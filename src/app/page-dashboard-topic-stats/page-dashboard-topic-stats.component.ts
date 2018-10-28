import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TopicStatsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import * as palette from 'google-palette';

type LineChartData = { x: any, y: number }[];
type BarChartData = number[];

@Component({
  templateUrl: './page-dashboard-topic-stats.component.html',
  styleUrls: [ './page-dashboard-topic-stats.component.scss' ]
})
export class PageDashboardTopicStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the topic
  @Input()
  public topicName: string;

  // Data subscription
  private dataSub: Subscription;

  // Update frequency
  private updateFrequency: number = 1;

  // Time buffer in seconds
  private timeBuffer: number = 20;

  // Sample buffer used to calculate derivatives
  public sampleBuffer: TopicStatsProductDTO[] = [];

  // Aggregate data
  public publishAggregateRates: LineChartData = [];
  public receiveAggregateRates: LineChartData = [];

  // Per member data
  private members: string[] = [];
  public publishMemberRates: BarChartData = [];
  public receiveMemberRates: BarChartData = [];
  public publishMemberTotal: BarChartData = [];
  public receiveMemberTotal: BarChartData = [];

  // Chart colors
  private propertyColors: string[] = [];
  private memberColors: string[] = [];

  // Chart objects
  private chartLinearPushRecvRate: Chart;
  private chartBarPushRecvMemberRate: Chart;
  private chartPiePushMember: Chart;
  private chartPieRecvMember: Chart;

  // Chart DOM hooks
  @ViewChild('chartLinearPushRecvRate')
  private chartLinearPushRecvRateElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartBarPushRecvMemberRate')
  private chartBarPushRecvMemberRateElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartPiePushMember')
  private chartPiePushMemberElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartPieRecvMember')
  private chartPieRecvMemberElementRef: ElementRef<HTMLCanvasElement>;

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

      this.dataSub = this.hazelcastService.subscribeToTopicStats(this.clustersService.getCurrentCluster().instanceName, this.topicName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<TopicStatsProductDTO>) => {
          const newData: TopicStatsProductDTO = notice.notice;

          const newMembers: string[] = Object.keys(newData.members);

          // Update the color palette
          if (newMembers.length !== this.members.length) {
            this.updateArray(
              this.memberColors,
              palette('tol-rainbow', newMembers.length).map(color => `#${color}`)
            );
          }

          // Update the members
          this.updateArray(this.members, newMembers);

          // Gather samples
          this.sampleBuffer.push(newData);

          // If we have enough to calculate the derivative
          if (this.sampleBuffer.length === 2) {
            const dt: number = (this.sampleBuffer[1].sampleTime - this.sampleBuffer[0].sampleTime) / 1000;

            // Line chart
            this.publishAggregateRates.push({
              x: this.sampleBuffer[0].sampleTime,
              y: (this.sampleBuffer[1].aggregated.publishOperationCount - this.sampleBuffer[0].aggregated.publishOperationCount) / dt
            });

            this.receiveAggregateRates.push({
              x: this.sampleBuffer[0].sampleTime,
              y: (this.sampleBuffer[1].aggregated.receiveOperationCount - this.sampleBuffer[0].aggregated.receiveOperationCount) / dt
            });

            // Bar chart
            this.updateArray(
              this.publishMemberRates,
              this.members.map(member => (this.sampleBuffer[1].members[member].publishOperationCount - this.sampleBuffer[0].members[member].publishOperationCount) / dt)
            );

            this.updateArray(
              this.receiveMemberRates,
              this.members.map(member => (this.sampleBuffer[1].members[member].receiveOperationCount - this.sampleBuffer[0].members[member].receiveOperationCount) / dt)
            );

            // Remove the oldest sample
            this.sampleBuffer.shift();

            // Rebuild the graph data
            this.updateCharts();
          }

          // Pie chart
          this.chartPiePushMember.options.title.text = [
            `Published: ${newData.aggregated.publishOperationCount} messages`,
          ];

          this.chartPieRecvMember.options.title.text = [
            `Received: ${newData.aggregated.receiveOperationCount} messages`
          ];

          this.updateArray(
            this.publishMemberTotal,
            this.members.map(member => newData.members[member].publishOperationCount)
          );

          this.updateArray(
            this.receiveMemberTotal,
            this.members.map(member => newData.members[member].receiveOperationCount)
          );
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the topic stats: ${error.errors}`);
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
    this.publishAggregateRates = [];
    this.receiveAggregateRates = [];

    // Per member data
    this.members = [];
    this.publishMemberRates = [];
    this.receiveMemberRates = [];
    this.publishMemberTotal = [];
    this.receiveMemberTotal = [];

    // Chart colors
    this.propertyColors = palette('tol', 2).map(color => `#${color}`);
    this.memberColors = [];

    this.chartPieRecvMember = new Chart(this.chartPieRecvMemberElementRef.nativeElement, {
      type: 'pie',
      data: {
        labels: this.members,
        datasets: [
          {
            data: this.receiveMemberTotal,
            backgroundColor: this.memberColors,
            label: 'Receives'
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

    this.chartPiePushMember = new Chart(this.chartPiePushMemberElementRef.nativeElement, {
      type: 'pie',
      data: {
        labels: this.members,
        datasets: [
          {
            data: this.publishMemberTotal,
            backgroundColor: this.memberColors,
            label: 'Publishes'
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

    this.chartBarPushRecvMemberRate = new Chart(this.chartBarPushRecvMemberRateElementRef.nativeElement, {
      type: 'bar',
      data: {
        labels: this.members,
        datasets: [
          {
            data: this.publishMemberRates,
            backgroundColor: this.propertyColors[0],
            label: 'Publish'
          },
          {
            data: this.receiveMemberRates,
            backgroundColor: this.propertyColors[1],
            label: 'Receive'
          }
        ]
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

    this.chartLinearPushRecvRate = new Chart(this.chartLinearPushRecvRateElementRef.nativeElement, {
      type: 'line',
      data: {
        datasets: [
          {
            data: this.publishAggregateRates,
            borderColor: this.propertyColors[0],
            label: 'Publish'
          },
          {
            data: this.receiveAggregateRates,
            borderColor: this.propertyColors[1],
            label: 'Receive'
          }
        ]
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
    if (this.chartPieRecvMember !== undefined) {
      this.chartPieRecvMember.destroy();
      this.chartPiePushMember.destroy();
      this.chartBarPushRecvMemberRate.destroy();
      this.chartLinearPushRecvRate.destroy();

      this.chartPieRecvMember = undefined;
      this.chartPiePushMember = undefined;
      this.chartBarPushRecvMemberRate = undefined;
      this.chartLinearPushRecvRate = undefined;
    }
  }

  private updateCharts(): void {
    // Trim the data in excess
    // TODO: This is not correct if the updateFrequency is bigger than the cap
    const pushRateTimeSpan: number = (this.publishAggregateRates[this.publishAggregateRates.length - 1].x - this.publishAggregateRates[0].x) / 1000;
    if (pushRateTimeSpan > this.timeBuffer) {
      this.publishAggregateRates.shift();
    }

    const recvRateTimeSpan: number = (this.receiveAggregateRates[this.receiveAggregateRates.length - 1].x - this.receiveAggregateRates[0].x) / 1000;
    if (recvRateTimeSpan > this.timeBuffer) {
      this.receiveAggregateRates.shift();
    }

    // Update
    this.chartLinearPushRecvRate.update();
    this.chartBarPushRecvMemberRate.update();
    this.chartPiePushMember.update();
    this.chartPieRecvMember.update();
  }

  // Copies src to dst
  private updateArray(dst: any[], src: any[]) {
    let i = 0;
    while (i < src.length) {
      if (i < dst.length) {
        // Update
        dst[i] = src[i];
      } else {
        // Mutate
        dst.push(src[i]);
      }
      i++;
    }

    // Clip eventual excess items of dst
    dst.length = src.length;
  }
}
