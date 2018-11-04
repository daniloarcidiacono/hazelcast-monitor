import {Chart} from 'chart.js';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';
import {PropertyTracker} from './property-tracker';
import {StatsProductDTO} from '@shared/dto/topic-products.dto';
import {StatisticsUtils} from '@shared/utils/stats.utils';
import * as palette from 'google-palette';

export interface PropertiesDescriptor {
  label: string;
  unit: string;
  color: string;
}

export interface ChartDescriptor {
  element: HTMLCanvasElement;
  properties: string[];
  rate: boolean;
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
            label: descriptor.properties[property].label
          }))
        )
      );
    }

    for (const memberserie of descriptor.memberseries) {
      this.charts.push(
        this.createMemberseriesChart(
          memberserie.element,
          memberserie.properties.map(property => ({
            data: this.propertyTracker.trackMembersProperty(property, memberserie.rate),
            backgroundColor: memberserie.rate ? descriptor.properties[property].color : this.memberColors,
            label: descriptor.properties[property].label
          })),
          this.propertyTracker.getMembers(),
          memberserie.rate
        )
      );
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

  private createTimeseriesChart(element: HTMLCanvasElement, datasets: any[]): Chart {
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
                labelString: 'time',
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
                labelString: 'ops/s',
                display: true
              }
            }
          ]
        },
        tooltips: {
          intersect: false,
          callbacks: {
            title: this.titleDateFormattingFn,
            label: this.labelRateFormattingFn
          }
        }
      }
    });
  }

  private createMemberseriesChart(element: HTMLCanvasElement, datasets: any[], members: string[], rate: boolean): Chart {
    if (rate) {
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
                  labelString: 'ops/s',
                  display: true
                }
              }
            ]
          },
          tooltips: {
            callbacks: {
              label: this.labelRateFormattingFn
            }
          }
        }
      });
    }

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
            label: this.labelValueFormattingFn
          }
        }
      }
    });
  }

  private titleDateFormattingFn = (item, data): string => {
    return this.dateFormatPipe.transform(this.localTimePipe.transform(item.xLabel), 'HH:mm:ss');
  };

  private labelRateFormattingFn = (item, data): string => {
    const y: number = Math.round(item.yLabel * 100) / 100;
    return `${data.datasets[item.datasetIndex].label}: ${y} ops/s`;
  };

  private labelValueFormattingFn = (item, data): string => {
    return `${data.datasets[item.datasetIndex].label}: ${data.labels[item.index]}: ${data.datasets[item.datasetIndex].data[item.index]}`;
  };
}
