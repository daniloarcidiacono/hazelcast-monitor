import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {MapsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/index';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {MapsTableModel} from './page-dashboard-maps.model';

@Component({
  templateUrl: './page-dashboard-maps.component.html',
  styleUrls: [ './page-dashboard-maps.component.scss' ]
})
export class PageDashboardMapsComponent implements OnDestroy {
  private dataSub: Subscription;
  private data: MapsProductDTO = undefined;
  public tableModel: MapsTableModel = new MapsTableModel();

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private router: Router) {
    this.dataSub = this.hazelcastService.subscribeToMaps(this.clustersService.getCurrentCluster().instanceName).subscribe(
      (notice: SubscriptionNoticeResponseDTO<MapsProductDTO>) => {
        this.data = notice.notice;
        this.tableModel.maps = this.data;
      },
      (error: ErrorMessageDTO) => {
        this.snackbarService.show(`Could not fetch the maps: ${error.errors}`);
      }
    );
  }

  public ngOnDestroy(): void {
    this.dataSub.unsubscribe();
  }

  public navigateToMapDetails(row: number): void {
    const mapName: string = this.tableModel.maps.maps[row].name;
    this.router.navigate([
      '/dashboard',
      {
        outlets: {
          'section': ['maps', mapName]
        }
      }
    ]);

    this.snackbarService.show(`Clicked on ${mapName}`);
  }

  public get clusterName(): string {
    return this.clustersService.getCurrentCluster().instanceName;
  }
}
