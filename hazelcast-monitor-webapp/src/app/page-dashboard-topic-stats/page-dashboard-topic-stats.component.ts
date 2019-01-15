import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TopicStatsDTO,} from '@shared/dto/topic-products.dto';
import {TopicStatsProductDTO} from '@shared/dto/topic-products-aliases.dto';
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
  private chartPiePushMember: Chart;

  @ViewChild('chartPieRecvMember')
  private chartPieRecvMemberElementRef: ElementRef<HTMLCanvasElement>;
  private chartPieRecvMember: Chart;

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

          // Update pie chart titles
          this.chartPiePushMember.options.title.text = [
            `${this.data.aggregated.publishOperationCount} publishes`,
          ];

          this.chartPieRecvMember.options.title.text = [
            `${this.data.aggregated.receiveOperationCount} receives`
          ];

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
            unit: 'ops',
            color: propertyColors[0]
          },

          'receiveOperationCount': {
            label: 'Receives',
            unit: 'ops',
            color: propertyColors[1]
          }
        },

        timeseries: [
          {
            element: this.chartLinearPushRecvRateElementRef.nativeElement,
            properties: [ 'publishOperationCount', 'receiveOperationCount' ],
            rate: true,
            yLabel: 'Operations per second'
          }
        ],

        memberseries: [
          {
            element: this.chartPiePushMemberElementRef.nativeElement,
            properties: [ 'publishOperationCount' ],
            rate: false,
            yLabel: 'Operations'
          },
          {
            element: this.chartPieRecvMemberElementRef.nativeElement,
            properties: [ 'receiveOperationCount' ],
            rate: false,
            yLabel: 'Operations'
          },
          {
            element: this.chartBarPushRecvMemberRateElementRef.nativeElement,
            properties: [ 'publishOperationCount', 'receiveOperationCount' ],
            rate: true,
            yLabel: 'Operations'
          }
        ]
      }
    );

    this.chartPiePushMember = this.statisticsEngine.chartOf(this.chartPiePushMemberElementRef.nativeElement);
    this.chartPieRecvMember = this.statisticsEngine.chartOf(this.chartPieRecvMemberElementRef.nativeElement);
  }
}
