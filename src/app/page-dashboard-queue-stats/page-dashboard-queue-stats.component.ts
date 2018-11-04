import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import {QueueStatsDTO, QueueStatsProductDTO} from '@shared/dto/topic-products.dto';
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
  private data: QueueStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

  // Update frequency
  private updateFrequency: number = 1;

  // Chart handler
  private statisticsEngine: StatisticsEngine<QueueStatsDTO>;

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
            unit: 'ops/s',
            color: propertyColors[0]
          },

          'rejectedOfferOperationCount': {
            label: 'Rejected offers',
            unit: 'ops/s',
            color: propertyColors[1]
          },

          'pollOperationCount': {
            label: 'Total polls',
            unit: 'ops/s',
            color: propertyColors[2]
          },

          'emptyPollOperationCount': {
            label: 'Empty polls',
            unit: 'ops/s',
            color: propertyColors[3]
          },

          'otherOperationsCount': {
            label: 'Others',
            unit: 'ops/s',
            color: propertyColors[4]
          },

          'eventOperationCount': {
            label: 'Events',
            unit: 'ops/s',
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
            rate: true
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
            rate: true
          },
          {
            element: this.chartOfferMemberElementRef.nativeElement,
            properties: [
              'offerOperationCount'
            ],
            rate: false
          },
          {
            element: this.chartPollMemberElementRef.nativeElement,
            properties: [
              'pollOperationCount'
            ],
            rate: false
          },
          {
            element: this.chartOtherMemberElementRef.nativeElement,
            properties: [
              'otherOperationsCount'
            ],
            rate: false
          },
          {
            element: this.chartEventMemberElementRef.nativeElement,
            properties: [
              'eventOperationCount'
            ],
            rate: false
          }
        ]
      }
    );
  }
}
