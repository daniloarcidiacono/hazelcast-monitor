import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from "rxjs/index";
import {MapProductDTO} from "@shared/dto/topic-products.dto";
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from "@shared/dto/hazelcast-monitor.dto";
import {MapTableModel} from "./page-dashboard-map.model";
// import {MapsTableModel} from './page-dashboard-maps.model';

@Component({
  templateUrl: './page-dashboard-map.component.html',
  styleUrls: [ './page-dashboard-map.component.scss' ]
})
export class PageDashboardMapComponent implements OnDestroy {
  private dataSub: Subscription;
  // private data: MapsProductDTO = undefined;
  public tableModel: MapTableModel = new MapTableModel();

  private paramSub: Subscription;
  private mapName: string;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private router: Router,
                     private route: ActivatedRoute) {
    this.paramSub = this.route.params.subscribe(params => {
      this.mapName = params['name'];
      this.initSubscription();
    });
  }

  private initSubscription(): void {
    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
    }

    this.dataSub = this.hazelcastService.subscribeToMap(this.clustersService.getCurrentCluster().instanceName, this.mapName).subscribe(
      (notice: SubscriptionNoticeResponseDTO<MapProductDTO>) => {
        this.tableModel.map = notice.notice;
        // this.data = notice.notice;
        // this.tableModel.maps = this.data;
      },
      (error: ErrorMessageDTO) => {
        this.snackbarService.show(`Could not fetch the map: ${error.errors}`);
      }
    );
  }

  public ngOnDestroy(): void {
    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
    }

    this.paramSub.unsubscribe();
  }

  public goBack(): void {
    this.router.navigate([
      '/dashboard',
      {
        outlets: {
          'section': 'maps'
        }
      }
    ]);
  }
}
