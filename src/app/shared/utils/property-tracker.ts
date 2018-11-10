import {StatsProductDTO} from '@shared/dto/topic-products.dto';
import {StatisticsUtils} from '@shared/utils/stats.utils';
import * as _ from 'lodash';

enum SeriesDataType {
  VALUE,
  RATE
}

export type TimeseriesData = {
  // Time
  x: number;

  // Value
  y: number;
}[];

interface Timeseries {
  // Property to track
  property: string;

  // If undefined, the timeseries values are picked from aggregated data, otherwise the specified member is picked
  member?: string;

  // Type of data (value or rate)
  type: SeriesDataType;

  // Data
  data: TimeseriesData;
}

export type MemberseriesData = number[];

interface Memberseries {
  // Property to track
  property: string;

  // Type of data (value or rate)
  type: SeriesDataType;

  // Data
  data: MemberseriesData;
}

// Helper class that keeps track of the properties of a statistic sample (either absolute or rate, aggregated or per member)
export class PropertyTracker<S> {
  private timeseries: Timeseries[] = [];
  private memberseries: Memberseries[] = [];
  private sampleBuffer: StatsProductDTO<S>[] = [];
  private members: string[] = [];
  private maxTimeSpan: number;

  public constructor(maxTimeSpan: number) {
    this.maxTimeSpan = maxTimeSpan;
  }

  // Tracks a property of the aggregated data
  public trackGlobalProperty(property: string, rate: boolean): TimeseriesData {
    const timeserie: Timeseries = {
      property: property,
      type: rate === true ? SeriesDataType.RATE : SeriesDataType.VALUE,
      data: []
    };

    this.timeseries.push(timeserie);
    return timeserie.data;
  }

  // Tracks a property of a specific member
  public trackMemberProperty(property: string, member: string, rate: boolean): TimeseriesData {
    const timeserie: Timeseries = {
      property: property,
      member: member,
      type: rate === true ? SeriesDataType.RATE : SeriesDataType.VALUE,
      data: []
    };

    this.timeseries.push(timeserie);
    return timeserie.data;
  }

  // Tracks a property across all members
  public trackMembersProperty(property: string, rate: boolean): MemberseriesData {
    const memberserie: Memberseries = {
      property: property,
      type: rate === true ? SeriesDataType.RATE : SeriesDataType.VALUE,
      data: []
    };

    this.memberseries.push(memberserie);
    return memberserie.data;
  }

  // Resets the states
  public reset(): void {
    this.sampleBuffer = [];
    this.timeseries = [];
    this.memberseries = [];
    this.members = [];
  }

  // Process a new sample, updating the timeseries
  public processSample(sample: StatsProductDTO<S>): void {
    this.updateMembers(sample);
    this.updateValueMemberSeries(sample);
    this.updateValueTimeSeries(sample);

    // Gather samples
    this.sampleBuffer.push(sample);

    // If we have enough samples
    if (this.sampleBuffer.length === 2) {
      this.updateRateMemberSeries();
      this.updateRateTimeSeries();

      // Discard the oldest sample
      this.sampleBuffer.shift();
    }

    // Trim
    this.trimTimeSeries();
  }

  private updateMembers(sample: StatsProductDTO<S>): void {
    const newMembers: string[] = Object.keys(sample.members);
    StatisticsUtils.updateArray(this.members, newMembers);
  }

  private updateValueMemberSeries(sample: StatsProductDTO<S>): void {
    for (const memberserie of this.memberseries) {
      if (memberserie.type === SeriesDataType.VALUE) {
        StatisticsUtils.updateArray(
          memberserie.data,
          Object.values(sample.members).map(member => {
            return _.get(member, memberserie.property, 0);
          })
        );
      }
    }
  }

  private updateValueTimeSeries(sample: StatsProductDTO<S>): void {
    for (const timeserie of this.timeseries) {
      if (timeserie.type === SeriesDataType.VALUE) {
        let value: number;

        // Member-specific time series
        if (timeserie.member !== undefined)  {
          if (sample.members.hasOwnProperty(timeserie.member)) {
            value = _.get(sample.members[timeserie.member], timeserie.property);
          }
        } else {
          // Aggregated time series
          value = _.get(sample.aggregated, timeserie.property);
        }

        if (value !== undefined) {
          timeserie.data.push({
            x: sample.sampleTime,
            y: value
          });
        }
      }
    }
  }

  private updateRateTimeSeries(): void {
    // We calculate rates per second
    const dt: number = (this.sampleBuffer[1].sampleTime - this.sampleBuffer[0].sampleTime) / 1000;

    for (const timeserie of this.timeseries) {
      if (timeserie.type === SeriesDataType.RATE) {
        let dv: number;

        // Member-specific time series
        if (timeserie.member !== undefined) {
          if (this.sampleBuffer[0].members.hasOwnProperty(timeserie.member) && this.sampleBuffer[1].members.hasOwnProperty(timeserie.member)) {
            dv = _.get(this.sampleBuffer[1].members[timeserie.member], timeserie.property) -
                 _.get(this.sampleBuffer[0].members[timeserie.member], timeserie.property);
          }
        } else {
          // Aggregated time series
          dv = _.get(this.sampleBuffer[1].aggregated, timeserie.property) -
               _.get(this.sampleBuffer[0].aggregated, timeserie.property);
        }

        if (!isNaN(dv)) {
          timeserie.data.push({
            x: this.sampleBuffer[0].sampleTime,
            y: dv / dt
          });
        }
      }
    }
  }

  private updateRateMemberSeries(): void {
    // We calculate rates per second
    const dt: number = (this.sampleBuffer[1].sampleTime - this.sampleBuffer[0].sampleTime) / 1000;

    for (const memberserie of this.memberseries) {
      if (memberserie.type === SeriesDataType.RATE) {
        StatisticsUtils.updateArray(
          memberserie.data,
          this.members.map(member => {
            let dv: number;
            if (this.sampleBuffer[0].members.hasOwnProperty(member) &&
                this.sampleBuffer[1].members.hasOwnProperty(member)) {
              dv = _.get(this.sampleBuffer[1].members[member], memberserie.property) -
                   _.get(this.sampleBuffer[0].members[member], memberserie.property);
            }

            return !isNaN(dv) ? dv / dt : 0;
          })
        );
      }
    }
  }

  private trimTimeSeries(): void {
    for (const timeserie of this.timeseries) {
      while (timeserie.data.length > 0 && (timeserie.data[timeserie.data.length - 1].x - timeserie.data[0].x) / 1000 > this.maxTimeSpan) {
        timeserie.data.shift();
      }
    }
  }

  public getMaxTimeSpan(): number {
    return this.maxTimeSpan;
  }

  public setMaxTimeSpan(value: number): void {
    if (value !== this.maxTimeSpan) {
      this.maxTimeSpan = value;
      this.trimTimeSeries();
    }
  }

  public getMembers(): string[] {
    return this.members;
  }
}
