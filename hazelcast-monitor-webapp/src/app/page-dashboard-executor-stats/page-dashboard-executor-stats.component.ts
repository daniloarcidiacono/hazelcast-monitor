import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {ExecutorStatsDTO, ExecutorStatsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import * as palette from 'google-palette';
import {StatisticsEngine} from '../shared/utils/statistics-engine';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';

@Component({
  templateUrl: './page-dashboard-executor-stats.component.html',
  styleUrls: [ './page-dashboard-executor-stats.component.scss' ]
})
export class PageDashboardExecutorStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the executor
  @Input()
  public executorName: string;

  // Current data
  private data: ExecutorStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

  // Update frequency
  private updateFrequency: number = 1;

  // Chart handler
  private statisticsEngine: StatisticsEngine<ExecutorStatsDTO>;

  // Chart DOM hooks
  @ViewChild('chartLatency')
  private chartLatencyElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartCounters')
  private chartCountersElementRef: ElementRef<HTMLCanvasElement>;

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

      this.dataSub = this.hazelcastService.subscribeToExecutorStats(this.clustersService.getCurrentCluster().instanceName, this.executorName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<ExecutorStatsProductDTO>) => {
          this.data = notice.notice;

          // Update the properties
          this.statisticsEngine.processSample(this.data);

          // Rebuild the graph data
          this.statisticsEngine.updateCharts();
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the executor stats: ${error.errors}`);
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
          'pendingTaskCount': {
            label: 'Pending',
            unit: 'tasks',
            color: propertyColors[0]
          },

          'startedTaskCount': {
            label: 'Started',
            unit: 'tasks',
            color: propertyColors[1]
          },

          'completedTaskCount': {
            label: 'Completed',
            unit: 'tasks',
            color: propertyColors[2]
          },

          'cancelledTaskCount': {
            label: 'Cancelled',
            unit: 'tasks',
            color: propertyColors[3]
          },

          'totalStartLatency': {
            label: 'Total start latency',
            unit: 'ms',
            color: propertyColors[4]
          },

          'totalExecutionLatency': {
            label: 'Total execution latency',
            unit: 'ms',
            color: propertyColors[5]
          }
        },

        timeseries: [
          {
            element: this.chartLatencyElementRef.nativeElement,
            properties: [ 'totalStartLatency', 'totalExecutionLatency' ],
            rate: false,
            yLabel: 'Latency (ms)'
          }
        ],

        memberseries: [
          {
            element: this.chartCountersElementRef.nativeElement,
            properties: [ 'pendingTaskCount', 'startedTaskCount', 'completedTaskCount', 'cancelledTaskCount' ],
            rate: false,
            yLabel: 'Tasks'
          }
        ]
      }
    );
  }
}
