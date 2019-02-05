import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import {QueueStatsDTO} from '@shared/dto/topic-products.dto';
import {QueueStatsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import * as palette from 'google-palette';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';
import {StatisticsEngine} from '../shared/utils/statistics-engine';

@Component({
  templateUrl: './page-dashboard-queue-stats.component.html',
  styleUrls: [ './page-dashboard-queue-stats.component.scss' ]
})
export class PageDashboardQueueStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the queue
  @Input()
  public queueName: string;

  // Current data
  public data: QueueStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

  // Update frequency
  private updateFrequency: number = 1;

  // Chart handler
  public statisticsEngine: StatisticsEngine<QueueStatsDTO>;

  // Chart DOM hooks
  @ViewChild('chartTotalRate')
  private chartTotalRateElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartMemberRate')
  private chartMemberRateElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartOfferMember')
  private chartOfferMemberElementRef: ElementRef<HTMLCanvasElement>;
  private chartOfferMember: Chart;

  @ViewChild('chartPollMember')
  private chartPollMemberElementRef: ElementRef<HTMLCanvasElement>;
  private chartPollMember: Chart;

  @ViewChild('chartEventMember')
  private chartEventMemberElementRef: ElementRef<HTMLCanvasElement>;
  private chartEventMember: Chart;

  @ViewChild('chartOtherMember')
  private chartOtherMemberElementRef: ElementRef<HTMLCanvasElement>;
  private chartOtherMember: Chart;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private localTimePipe: LocalTimePipe,
                     private dateFormatPipe: DateFormatPipe) {
    this.statisticsEngine = new StatisticsEngine(localTimePipe, dateFormatPipe);
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

      this.dataSub = this.hazelcastService.subscribeToQueueStats(this.clustersService.getCurrentCluster().instanceName, this.queueName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<QueueStatsProductDTO>) => {
          this.data = notice.notice;

          // Update the properties
          this.statisticsEngine.processSample(this.data);

          // Update pie chart titles
          this.chartOfferMember.options.title.text = [
            `${this.data.aggregated.offerOperationCount} offers`
          ];

          this.chartPollMember.options.title.text = [
            `${this.data.aggregated.pollOperationCount} polls`
          ];

          this.chartEventMember.options.title.text = [
            `${this.data.aggregated.eventOperationCount} events`
          ];

          this.chartOtherMember.options.title.text = [
            `${this.data.aggregated.otherOperationsCount} other operations`
          ];

          // Rebuild the graph data
          this.statisticsEngine.updateCharts();
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the queue stats: ${error.errors}`);
        }
      );
    }
  }

  private unsubscribe(): void {
    this.tab.recording = false;
    this.statisticsEngine.destroyCharts();

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
    const propertyColors: string[] = palette('tol', 6).map(color => `#${color}`);
    this.statisticsEngine.initCharts(
      {
        properties: {
          'offerOperationCount': {
            label: 'Total offers',
            unit: 'ops',
            color: propertyColors[0]
          },

          'rejectedOfferOperationCount': {
            label: 'Rejected offers',
            unit: 'ops',
            color: propertyColors[1]
          },

          'pollOperationCount': {
            label: 'Total polls',
            unit: 'ops',
            color: propertyColors[2]
          },

          'emptyPollOperationCount': {
            label: 'Empty polls',
            unit: 'ops',
            color: propertyColors[3]
          },

          'otherOperationsCount': {
            label: 'Others',
            unit: 'ops',
            color: propertyColors[4]
          },

          'eventOperationCount': {
            label: 'Events',
            unit: 'ops',
            color: propertyColors[5]
          }
        },
        timeseries: [
          {
            element: this.chartTotalRateElementRef.nativeElement,
            properties: [
              'offerOperationCount',
              'rejectedOfferOperationCount',
              'pollOperationCount',
              'emptyPollOperationCount',
              'otherOperationsCount',
              'eventOperationCount'
            ],
            rate: true,
            yLabel: 'Operations per second'
          }
        ],
        memberseries: [
          {
            element: this.chartMemberRateElementRef.nativeElement,
            properties: [
              'offerOperationCount',
              'rejectedOfferOperationCount',
              'pollOperationCount',
              'emptyPollOperationCount',
              'otherOperationsCount',
              'eventOperationCount'
            ],
            rate: true,
            yLabel: 'Operations per second'
          },
          {
            element: this.chartOfferMemberElementRef.nativeElement,
            properties: [
              'offerOperationCount'
            ],
            rate: false,
            yLabel: 'Offers'
          },
          {
            element: this.chartPollMemberElementRef.nativeElement,
            properties: [
              'pollOperationCount'
            ],
            rate: false,
            yLabel: 'Polls'
          },
          {
            element: this.chartOtherMemberElementRef.nativeElement,
            properties: [
              'otherOperationsCount'
            ],
            rate: false,
            yLabel: 'Other operations'
          },
          {
            element: this.chartEventMemberElementRef.nativeElement,
            properties: [
              'eventOperationCount'
            ],
            rate: false,
            yLabel: 'Events'
          }
        ]
      }
    );

    this.chartOfferMember = this.statisticsEngine.chartOf(this.chartOfferMemberElementRef.nativeElement);
    this.chartPollMember = this.statisticsEngine.chartOf(this.chartPollMemberElementRef.nativeElement);
    this.chartEventMember = this.statisticsEngine.chartOf(this.chartEventMemberElementRef.nativeElement);
    this.chartOtherMember = this.statisticsEngine.chartOf(this.chartOtherMemberElementRef.nativeElement);
  }
}
