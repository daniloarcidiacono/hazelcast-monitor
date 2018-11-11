import {Component, ElementRef, Input, OnDestroy, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {MapStatsDTO, MapStatsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {Chart} from 'chart.js';
import * as palette from 'google-palette';
import {StatisticsEngine} from '../shared/utils/statistics-engine';
import {DateFormatPipe, LocalTimePipe} from 'ngx-moment';
import {DistributedObjectType} from '@shared/dto/topics.dto';

@Component({
  templateUrl: './page-dashboard-map-stats.component.html',
  styleUrls: [ './page-dashboard-map-stats.component.scss' ]
})
export class PageDashboardMapStatsComponent implements TabAwareComponent, OnDestroy {
  // Name of the map
  @Input()
  public mapName: string;

  // Type of map
  @Input()
  public mapType: DistributedObjectType;

  // Current data
  private data: MapStatsProductDTO = undefined;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

  // Update frequency
  private updateFrequency: number = 1;

  // Chart handler
  private statisticsEngine: StatisticsEngine<MapStatsDTO>;

  // Chart DOM hooks
  @ViewChild('chartRates')
  private chartRatesElementRef: ElementRef<HTMLCanvasElement>;

  @ViewChild('chartMemberRates')
  private chartMemberRatesElementRef: ElementRef<HTMLCanvasElement>;

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

      this.dataSub = this.hazelcastService.subscribeToDistributedObjectStats(this.clustersService.getCurrentCluster().instanceName, this.mapType,this.mapName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<MapStatsProductDTO>) => {
          this.data = notice.notice;

          // Update the properties
          this.statisticsEngine.processSample(this.data);

          // Update pie chart titles
          // this.chartOwned.options.title.text = [
          //   `${this.data.aggregated.ownedEntryCount} entries`
          // ];

          // Rebuild the graph data
          this.statisticsEngine.updateCharts();
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the map stats: ${error.errors}`);
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
    const propertyColors: string[] = palette('tol', 5).map(color => `#${color}`);
    this.statisticsEngine.initCharts(
      {
        properties: {
          'getOperationCount': {
            label: 'Gets',
            unit: 'ops',
            color: propertyColors[0]
          },
          'putOperationCount': {
            label: 'Puts',
            unit: 'ops',
            color: propertyColors[1]
          },
          'removeOperationCount': {
            label: 'Removes',
            unit: 'ops',
            color: propertyColors[2]
          },
          'eventOperationCount': {
            label: 'Events',
            unit: 'ops',
            color: propertyColors[3]
          },
          'otherOperationCount': {
            label: 'Others',
            unit: 'ops',
            color: propertyColors[4]
          }
        },

        timeseries: [
          {
            element: this.chartRatesElementRef.nativeElement,
            properties: [
              'getOperationCount',
              'putOperationCount',
              'removeOperationCount',
              'eventOperationCount',
              'otherOperationCount'
            ],
            rate: true,
            yLabel: 'Operations'
          }
        ],

        memberseries: [
          {
            element: this.chartMemberRatesElementRef.nativeElement,
            properties: [
              'getOperationCount',
              'putOperationCount',
              'removeOperationCount',
              'eventOperationCount',
              'otherOperationCount'
            ],
            rate: true,
            yLabel: 'Operations'
          },
          // {
          //   element: this.chartOperationsElementRef.nativeElement,
          //   properties: [ 'mapGets', 'mapPuts', 'mapRemovals', 'mapEvictions' ],
          //   rate: false,
          //   yLabel: 'Operations'
          // }
        ]
      }
    );

    // Get the chart references
    // this.chartOwned = this.statisticsEngine.chartOf(this.chartOwnedElementRef.nativeElement);

    // const chartRates: Chart = this.statisticsEngine.chartOf(this.chartRatesElementRef.nativeElement);
    // chartRates.options.scales.yAxes[0].ticks.min = 0;
    // chartRates.options.scales.yAxes[0].ticks.max = 100;
    // chartRates.options.scales.yAxes[0].ticks.callback = (tick) => tick.toString() + '%';
    // chartRates.update();
  }
}
