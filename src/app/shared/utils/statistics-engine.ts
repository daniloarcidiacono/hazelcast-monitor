import {Chart} from 'chart.js';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';
import {PropertyTracker} from './property-tracker';
import {StatsProductDTO} from '@shared/dto/topic-products.dto';
import {StatisticsUtils} from '@shared/utils/stats.utils';
import * as palette from 'google-palette';
import {isNumber} from 'util';

export interface PropertiesDescriptor {
  label: string;
  unit: string;
  color: string;
}

export interface ChartDescriptor {
  element: HTMLCanvasElement;
  properties: string[];
  rate: boolean;
  yLabel: string;
}

export interface StatisticsCharts {
  properties: { [ property: string ] : PropertiesDescriptor };
  timeseries: ChartDescriptor[];
  memberseries: ChartDescriptor[];
}

// Main statistics engine
// Handles chart objects and property tracking
export class StatisticsEngine<S> {
  // Time buffer in seconds
  private timeBuffer: number = 20;

  // Property tracker
  private propertyTracker: PropertyTracker<S> = new PropertyTracker(this.timeBuffer);

  // Chart colors
  private memberColors: string[] = [];

  // Chart objects
  private charts: Chart[] = [];

  public constructor(private localTimePipe: LocalTimePipe,
                     private dateFormatPipe: DateFormatPipe) {
  }

  public initCharts(descriptor: StatisticsCharts): void {
    // Reset
    this.propertyTracker.reset();
    this.memberColors = [];
    this.charts = [];

    for (const timeserie of descriptor.timeseries) {
      this.charts.push(
        this.createTimeseriesChart(
          timeserie.element,
          timeserie.properties.map(property => ({
            data: this.propertyTracker.trackGlobalProperty(property, timeserie.rate),
            borderColor: descriptor.properties[property].color,
            label: descriptor.properties[property].label,
            property: descriptor.properties[property],
            rate: timeserie.rate
          })),
          timeserie.yLabel
        )
      );
    }

    for (const memberserie of descriptor.memberseries) {
      if (memberserie.rate || memberserie.properties.length > 1) {
        this.charts.push(
          this.createMemberseriesBarChart(
            memberserie.element,
            memberserie.properties.map(property => ({
              data: this.propertyTracker.trackMembersProperty(property, memberserie.rate),
              backgroundColor: descriptor.properties[property].color,
              label: descriptor.properties[property].label,
              property: descriptor.properties[property],
              rate: memberserie.rate
            })),
            this.propertyTracker.getMembers(),
            memberserie.yLabel
          )
        );
      } else {
        this.charts.push(
          this.createMemberseriesPieChart(
            memberserie.element,
            memberserie.properties.map(property => ({
              data: this.propertyTracker.trackMembersProperty(property, memberserie.rate),
              backgroundColor: this.memberColors,
              label: descriptor.properties[property].label,
              property: descriptor.properties[property],
              rate: memberserie.rate
            })),
            this.propertyTracker.getMembers()
          )
        );
      }
    }
  }

  public processSample(sample: StatsProductDTO<S>): void {
    // Update the memebr colors
    StatisticsUtils.updateArray(
      this.memberColors,
      palette('tol-rainbow', this.propertyTracker.getMembers().length).map(color => `#${color}`)
    );

    this.propertyTracker.processSample(sample);
  }

  public destroyCharts(): void {
    for (const chart of this.charts) {
      chart.destroy();
    }

    this.charts = [];
  }

  public updateCharts(): void {
    for (const chart of this.charts) {
      chart.update();
    }
  }

  public getMaxTimeSpan(): number {
    return this.propertyTracker.getMaxTimeSpan();
  }

  public setMaxTimeSpan(value: number): void {
    this.propertyTracker.setMaxTimeSpan(value);
  }

  private createTimeseriesChart(element: HTMLCanvasElement, datasets: any[], yLabel: string): Chart {
    return new Chart(element, {
      type: 'line',
      data: {
        datasets: datasets
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: {
          duration: 0
        },
        legend: {
          position: 'bottom'
        },
        title: {
          text: 'Line chart',
          display: false
        },
        scales: {
          xAxes: [
            {
              type: 'time',
              position: 'bottom',
              ticks: {
                maxRotation: 0
              },
              scaleLabel: {
                labelString: 'Time',
                display: true
              }
            }
          ],
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              },
              scaleLabel: {
                labelString: yLabel,
                display: true
              }
            }
          ]
        },
        tooltips: {
          intersect: false,
          callbacks: {
            title: this.titleDateFormattingFn,
            label: this.labelFormattingFn
          }
        }
      }
    });
  }

  private createMemberseriesBarChart(element: HTMLCanvasElement, datasets: any[], members: string[], yLabel: string): Chart {
    return new Chart(element, {
      type: 'bar',
      data: {
        labels: members,
        datasets: datasets
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
          position: 'bottom'
        },
        title: {
          text: 'Bar chart',
          display: false
        },
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              },
              scaleLabel: {
                labelString: yLabel,
                display: true
              }
            }
          ]
        },
        tooltips: {
          callbacks: {
            label: this.labelFormattingFn
          }
        }
      }
    });
  }

  private createMemberseriesPieChart(element: HTMLCanvasElement, datasets: any[], members: string[]): Chart {
    return new Chart(element, {
      type: 'pie',
      data: {
        labels: members,
        datasets: datasets
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
          position: 'bottom',
          fullWidth: false
        },
        title: {
          text: '',
          display: true
        },
        tooltips: {
          callbacks: {
            label: this.labelFormattingFn
          }
        }
      }
    });
  }

  private titleDateFormattingFn = (item, data): string => {
    return this.dateFormatPipe.transform(this.localTimePipe.transform(item.xLabel), 'HH:mm:ss');
  };

  private labelFormattingFn = (item, data): string => {
    const point: number | any = data.datasets[item.datasetIndex].data[item.index];
    let value: number = isNumber(point) ? point : point.y;
    let unit: string = data.datasets[item.datasetIndex].property.unit;
    if (data.datasets[item.datasetIndex].rate) {
      value = Math.round(value * 100) / 100;
      unit = unit + '/s';
    }

    return `${data.datasets[item.datasetIndex].label}: ${value} ${unit}`;
  };
}
