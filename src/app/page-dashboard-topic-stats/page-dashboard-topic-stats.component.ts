import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TopicStatsDTO, TopicStatsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import * as palette from 'google-palette';
import {StatisticsEngine} from '../shared/utils/statistics-engine';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';

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

  // Chart handler
  private statisticsEngine: StatisticsEngine<TopicStatsDTO>;

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

      this.dataSub = this.hazelcastService.subscribeToTopicStats(this.clustersService.getCurrentCluster().instanceName, this.topicName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<TopicStatsProductDTO>) => {
          this.data = notice.notice;

          // Update the properties
          this.statisticsEngine.processSample(this.data);

          // Pie chart
          // this.chartPiePushMember.options.title.text = [
          //   `Published: ${this.data.aggregated.publishOperationCount} messages`,
          // ];
          //
          // this.chartPieRecvMember.options.title.text = [
          //   `Received: ${this.data.aggregated.receiveOperationCount} messages`
          // ];

          // Rebuild the graph data
          this.statisticsEngine.updateCharts();
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the topic stats: ${error.errors}`);
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
    const propertyColors: string[] = palette('tol', 2).map(color => `#${color}`);
    this.statisticsEngine.initCharts(
      {
        properties: {
          'publishOperationCount': {
            label: 'Publishes',
            unit: 'ops/s',
            color: propertyColors[0]
          },

          'receiveOperationCount': {
            label: 'Receives',
            unit: 'ops/s',
            color: propertyColors[1]
          }
        },

        timeseries: [
          {
            element: this.chartLinearPushRecvRateElementRef.nativeElement,
            properties: [ 'publishOperationCount', 'receiveOperationCount' ],
            rate: true
          }
        ],

        memberseries: [
          {
            element: this.chartPiePushMemberElementRef.nativeElement,
            properties: [ 'publishOperationCount' ],
            rate: false
          },

          {
            element: this.chartPieRecvMemberElementRef.nativeElement,
            properties: [ 'receiveOperationCount' ],
            rate: false
          },

          {
            element: this.chartBarPushRecvMemberRateElementRef.nativeElement,
            properties: [ 'publishOperationCount', 'receiveOperationCount' ],
            rate: true
          }
        ]
      }
    );
  }
}
