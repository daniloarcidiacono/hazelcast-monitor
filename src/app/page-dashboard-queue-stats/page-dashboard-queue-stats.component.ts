import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import {QueueStatsProductDTO} from '@shared/dto/topic-products.dto';
import {BarChartData, LineChartData, StatisticsUtils} from '@shared/utils/stats.utils';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import * as palette from 'google-palette';

class TimeSeries {
  public propertyName: string;

  // Aggregate data (size grows over time)
  public ratesData: LineChartData;

  // Per member rates data (constant size in time)
  public memberRatesData: BarChartData;

  // Per member total data (constant size in time)
  public memberTotalData: BarChartData;

  // Color
  public color: string;

  // Chart
  public chart: Chart;
  public chartElementRef: ElementRef<HTMLCanvasElement>;
  public label: string;

  public constructor(propertyName: string, label: string) {
    this.propertyName = propertyName;
    this.label = label;
    this.clear();
  }

  public trim(maxTimeSpan: number): void {
    const timeSpan: number = (this.ratesData[this.ratesData.length - 1].x - this.ratesData[0].x) / 1000;
    if (timeSpan > maxTimeSpan) {
      this.ratesData.shift();
    }
  }

  public updateChart(): void {
    if (this.chart !== undefined) {
      this.chart.update();
    }
  }
  public destroyChart(): void {
    if (this.chart !== undefined) {
      this.chart.destroy();
      this.chart = undefined;
    }
  }

  public setChartTitle(title: string): void {
    if (this.chart !== undefined) {
     this.chart.options.title.text = title;
    }
  }

  public createChart(chartElementRef: ElementRef<HTMLCanvasElement>, members: string[], memberColors: string[]): void {
    this.chartElementRef = chartElementRef;
    this.chart = new Chart(this.chartElementRef.nativeElement, {
      type: 'pie',
      data: {
        labels: members,
        datasets: [
          {
            data: this.memberTotalData,
            backgroundColor: memberColors,
            label: this.label
          }
        ]
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
            label: function (item, data) {
              return `${data.datasets[item.datasetIndex].label}: ${data.labels[item.index]}: ${data.datasets[item.datasetIndex].data[item.index]}`;
            }
          }
        }
      }
    });
  }

  public clear(): void {
    this.ratesData = [];
    this.memberRatesData = [];
    this.memberTotalData = [];
  }
}

@Component({
  templateUrl: './page-dashboard-queue-stats.component.html',
  styleUrls: [ './page-dashboard-queue-stats.component.scss' ]
})
export class PageDashboardQueueStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the queue
  @Input()
  public queueName: string;

  // Current data
  private data: QueueStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Update frequency
  private updateFrequency: number = 1;

  // Time buffer in seconds
  private timeBuffer: number = 20;

  // Sample buffer used to calculate derivatives
  public sampleBuffer: QueueStatsProductDTO[] = [];

  // Time series
  public timeseries: { [ propertyName: string ]: TimeSeries } = {
    offerOperationCount: new TimeSeries('offerOperationCount', 'Total offers'),
    rejectedOfferOperationCount: new TimeSeries('rejectedOfferOperationCount', 'Rejected offers'),
    pollOperationCount: new TimeSeries('pollOperationCount', 'Total polls'),
    emptyPollOperationCount: new TimeSeries('emptyPollOperationCount', 'Empty polls'),
    otherOperationsCount: new TimeSeries('otherOperationsCount', 'Others'),
    eventOperationCount: new TimeSeries('eventOperationCount', 'Events')
  };

  // Per member data
  private members: string[] = [];

  // Chart colors
  private memberColors: string[] = [];

  // Chart objects
  private chartTotalRate: Chart;
  private chartMemberRate: Chart;

  // Chart DOM hooks
  @ViewChild('chartTotalRate')
  private chartTotalRateElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartMemberRate')
  private chartMemberRateElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartOfferMember')
  private chartOfferMemberElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartPollMember')
  private chartPollMemberElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartEventMember')
  private chartEventMemberElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartOtherMember')
  private chartOtherMemberElementRef: ElementRef<HTMLCanvasElement>;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public beforeShow(): void {
    this.initCharts();

    if (!this.dataSub) {
      const parameters: any = {
        frequency: `${this.updateFrequency}`
      };

      this.dataSub = this.hazelcastService.subscribeToQueueStats(this.clustersService.getCurrentCluster().instanceName, this.queueName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<QueueStatsProductDTO>) => {
          this.data = notice.notice;
          const newMembers: string[] = Object.keys(this.data.members);

          if (newMembers.length !== this.members.length) {
            // Update the color palette
            StatisticsUtils.updateArray(
              this.memberColors,
              palette('tol-rainbow', newMembers.length).map(color => `#${color}`)
            );

            // When a member is added or removed, we must restart gathering the samples
            // This is because the bar chart updating will break if the old sample does not have a corresponding entry for the new member
            this.sampleBuffer = [];
          }

          // Update the members
          StatisticsUtils.updateArray(this.members, newMembers);

          // Gather samples
          this.sampleBuffer.push(this.data);

          // If we have enough to calculate the derivative
          if (this.sampleBuffer.length === 2) {
            const dt: number = (this.sampleBuffer[1].sampleTime - this.sampleBuffer[0].sampleTime) / 1000;

            for (const property in this.timeseries) {
              // Line chart
              this.timeseries[property].ratesData.push({
                  x: this.sampleBuffer[0].sampleTime,
                  y: (this.sampleBuffer[1].aggregated[property] - this.sampleBuffer[0].aggregated[property]) / dt
              });

              // Bar chart
              StatisticsUtils.updateArray(
                this.timeseries[property].memberRatesData,
                this.members.map(member => (this.sampleBuffer[1].members[member][property] - this.sampleBuffer[0].members[member][property]) / dt)
              );
            }

            // Remove the oldest sample
            this.sampleBuffer.shift();

            // Rebuild the graph data
            this.updateCharts();
          }

          // Pie chart
          for (const property in this.timeseries) {
            this.timeseries[property].setChartTitle(`${this.timeseries[property].label}: ${this.data.aggregated[property]}`);
            StatisticsUtils.updateArray(
              this.timeseries[property].memberTotalData,
              this.members.map(member => this.data.members[member][property])
            );
          }
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the queue stats: ${error.errors}`);
        }
      );
    }
  }

  public beforeHide(): void {
    this.destroyCharts();

    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
      this.dataSub = undefined;
    }
  }

  public tabCreated(tab: TabData): void {
  }

  private initCharts(): void {
    // Reset
    this.sampleBuffer = [];

    // Aggregate data
    const propertyColors: string[] = palette('tol', Object.keys(this.timeseries).length).map(color => `#${color}`);
    let index: number = 0;

    for (const property in this.timeseries) {
      this.timeseries[property].color = propertyColors[index];
      this.timeseries[property].clear();
      index++;
    }

    // Per member data
    this.members = [];

    // Chart colors
    this.memberColors = [];

    this.timeseries['offerOperationCount'].createChart(this.chartOfferMemberElementRef, this.members, this.memberColors);
    this.timeseries['pollOperationCount'].createChart(this.chartPollMemberElementRef, this.members, this.memberColors);
    this.timeseries['otherOperationsCount'].createChart(this.chartOtherMemberElementRef, this.members, this.memberColors);
    this.timeseries['eventOperationCount'].createChart(this.chartEventMemberElementRef, this.members, this.memberColors);

    this.chartMemberRate = new Chart(this.chartMemberRateElementRef.nativeElement, {
      type: 'bar',
      data: {
        labels: this.members,
        datasets:
          Object.values(this.timeseries).map(
            series => (
              {
                data: series.memberRatesData,
                backgroundColor: series.color,
                label: series.label
              }
            )
          )
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
        }
      }
    });

    this.chartTotalRate = new Chart(this.chartTotalRateElementRef.nativeElement, {
      type: 'line',
      data: {
        datasets:
          Object.values(this.timeseries).map(
            series => (
              {
                data: series.ratesData,
                borderColor: series.color,
                label: series.label
              }
            )
          )
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
        }
      }
    });
  }

  private destroyCharts(): void {
    for (const property in this.timeseries) {
      this.timeseries[property].destroyChart();
    }

    if (this.chartTotalRate !== undefined) {
      this.chartTotalRate.destroy();
      this.chartMemberRate.destroy();

      this.chartTotalRate = undefined;
      this.chartMemberRate = undefined;
    }
  }

  private updateCharts(): void {
    // Trim the data in excess
    // TODO: This is not correct if the updateFrequency is bigger than the cap
    for (const property in this.timeseries) {
      this.timeseries[property].trim(this.timeBuffer);
    }

    // Update
    this.chartTotalRate.update();
    this.chartMemberRate.update();

    for (const property in this.timeseries) {
      this.timeseries[property].updateChart();
    }
  }
}
