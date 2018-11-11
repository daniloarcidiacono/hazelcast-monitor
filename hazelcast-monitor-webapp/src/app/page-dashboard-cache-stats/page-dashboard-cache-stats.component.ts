import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {CacheStatsDTO, CacheStatsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import * as palette from 'google-palette';
import {StatisticsEngine} from '../shared/utils/statistics-engine';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';

@Component({
  templateUrl: './page-dashboard-cache-stats.component.html',
  styleUrls: [ './page-dashboard-cache-stats.component.scss' ]
})
export class PageDashboardCacheStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the cache
  @Input()
  public cacheName: string;

  // Current data
  private data: CacheStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

  // Update frequency
  private updateFrequency: number = 1;

  // Chart handler
  private statisticsEngine: StatisticsEngine<CacheStatsDTO>;

  // Chart DOM hooks
  @ViewChild('chartRates')
  private chartRatesElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartOwned')
  private chartOwnedElementRef: ElementRef<HTMLCanvasElement>;
  private chartOwned: Chart;

  @ViewChild('chartOperations')
  private chartOperationsElementRef: ElementRef<HTMLCanvasElement>;

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

      this.dataSub = this.hazelcastService.subscribeToCacheStats(this.clustersService.getCurrentCluster().instanceName, this.cacheName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<CacheStatsProductDTO>) => {
          this.data = notice.notice;

          // Update the properties
          this.statisticsEngine.processSample(this.data);

          // Update pie chart titles
          this.chartOwned.options.title.text = [
            `${this.data.aggregated.ownedEntryCount} entries`
          ];

          // Rebuild the graph data
          this.statisticsEngine.updateCharts();
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the cache stats: ${error.errors}`);
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
    const propertyColors: string[] = palette('tol', 7).map(color => `#${color}`);
    this.statisticsEngine.initCharts(
      {
        properties: {
          'cacheHitPercentage': {
            label: 'Hits',
            unit: '%',
            color: propertyColors[0]
          },

          'cacheMissPercentage': {
            label: 'Misses',
            unit: '%',
            color: propertyColors[1]
          },

          'ownedEntryCount': {
            label: 'Owned entries',
            unit: 'entries',
            color: propertyColors[2]
          },

          'cacheGets': {
            label: 'Gets',
            unit: 'ops',
            color: propertyColors[3]
          },

          'cachePuts': {
            label: 'Puts',
            unit: 'ops',
            color: propertyColors[4]
          },

          'cacheRemovals': {
            label: 'Removals',
            unit: 'ops',
            color: propertyColors[5]
          },

          'cacheEvictions': {
            label: 'Evictions',
            unit: 'ops',
            color: propertyColors[6]
          }
        },

        timeseries: [
          {
            element: this.chartRatesElementRef.nativeElement,
            properties: [ 'cacheHitPercentage', 'cacheMissPercentage' ],
            rate: false,
            yLabel: 'Percentage'
          }
        ],

        memberseries: [
          {
            element: this.chartOwnedElementRef.nativeElement,
            properties: [ 'ownedEntryCount' ],
            rate: false,
            yLabel: 'Owned entries'
          },
          {
            element: this.chartOperationsElementRef.nativeElement,
            properties: [ 'cacheGets', 'cachePuts', 'cacheRemovals', 'cacheEvictions' ],
            rate: false,
            yLabel: 'Operations'
          }
        ]
      }
    );

    // Get the chart references
    this.chartOwned = this.statisticsEngine.chartOf(this.chartOwnedElementRef.nativeElement);

    const chartRates: Chart = this.statisticsEngine.chartOf(this.chartRatesElementRef.nativeElement);
    chartRates.options.scales.yAxes[0].ticks.min = 0;
    chartRates.options.scales.yAxes[0].ticks.max = 100;
    chartRates.options.scales.yAxes[0].ticks.callback = (tick) => tick.toString() + '%';
    chartRates.update();
  }
}
