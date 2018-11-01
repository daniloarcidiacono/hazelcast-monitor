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
import {BarChartData, LineChartData, StatisticsUtils} from '@shared/utils/stats.utils';
import {DateFormatPipe, LocalTimePipe} from "ngx-moment";

@Component({
  templateUrl: './page-dashboard-topic-stats.component.html',
  styleUrls: [ './page-dashboard-topic-stats.component.scss' ]
})
export class PageDashboardTopicStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the topic
  @Input()
  public topicName: string;

  // Current data
  private data: TopicStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

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
                     private hazelcastService: SharedHazelcastAgentService,
                     private localTimePipe: LocalTimePipe,
                     private dateFormatPipe: DateFormatPipe) {
  }

  public ngOnDestroy(): void {
    this.unsubscribe();
  }

  public beforeShow(): void {
  }

  public beforeHide(): void {
  }

  private subscribe(): void {
    this.tab.recording = true;
    this.initCharts();

    if (!this.dataSub) {
      const parameters: any = {
        frequency: `${this.updateFrequency}`
      };

      this.dataSub = this.hazelcastService.subscribeToTopicStats(this.clustersService.getCurrentCluster().instanceName, this.topicName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<TopicStatsProductDTO>) => {
          this.data = notice.notice;
          const newMembers: string[] = Object.keys(this.data.members);

          // Update the color palette
          if (newMembers.length !== this.members.length) {
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
            StatisticsUtils.updateArray(
              this.publishMemberRates,
              this.members.map(member => (this.sampleBuffer[1].members[member].publishOperationCount - this.sampleBuffer[0].members[member].publishOperationCount) / dt)
            );

            StatisticsUtils.updateArray(
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
            `Published: ${this.data.aggregated.publishOperationCount} messages`,
          ];

          this.chartPieRecvMember.options.title.text = [
            `Received: ${this.data.aggregated.receiveOperationCount} messages`
          ];

          StatisticsUtils.updateArray(
            this.publishMemberTotal,
            this.members.map(member => this.data.members[member].publishOperationCount)
          );

          StatisticsUtils.updateArray(
            this.receiveMemberTotal,
            this.members.map(member => this.data.members[member].receiveOperationCount)
          );
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the topic stats: ${error.errors}`);
        }
      );
    }
  }

  private unsubscribe(): void {
    this.tab.recording = false;
    this.destroyCharts();

    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
      this.dataSub = undefined;
    }
  }

  public tabCreated(tab: TabData): void {
    this.tab = tab;
    this.subscribe();
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
        },
        tooltips: {
          callbacks: {
            label: (item, data) => {
              const y: number = Math.round(item.yLabel * 100) / 100;
              return `${data.datasets[item.datasetIndex].label}: ${y} ops/s`;
            }
          }
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
            title: (item, data) => {
              const x: string = this.dateFormatPipe.transform(this.localTimePipe.transform(item.xLabel), 'HH:mm:ss');
              return x;
            },
            label: (item, data) => {
              const y: number = Math.round(item.yLabel * 100) / 100;
              return `${data.datasets[item.datasetIndex].label}: ${y} ops/s`;
            }
          }
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
    while (this.publishAggregateRates.length > 0 && (this.publishAggregateRates[this.publishAggregateRates.length - 1].x - this.publishAggregateRates[0].x) / 1000 > this.timeBuffer) {
      this.publishAggregateRates.shift();
    }

    while (this.receiveAggregateRates.length > 0 && (this.receiveAggregateRates[this.receiveAggregateRates.length - 1].x - this.receiveAggregateRates[0].x) / 1000 > this.timeBuffer) {
      this.receiveAggregateRates.shift();
    }

    // Update
    this.chartLinearPushRecvRate.update();
    this.chartBarPushRecvMemberRate.update();
    this.chartPiePushMember.update();
    this.chartPieRecvMember.update();
  }
}
