import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {MembersProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/index';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {MembersTableModel} from './page-dashboard-members.model';

@Component({
  templateUrl: './page-dashboard-members.component.html',
  styleUrls: [ './page-dashboard-members.component.scss' ]
})
export class PageDashboardMembersComponent implements OnDestroy {
  private dataSub: Subscription;
  private data: MembersProductDTO = undefined;
  public tableModel: MembersTableModel = new MembersTableModel();

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private router: Router) {
    this.dataSub = this.hazelcastService.subscribeToMembers(this.clustersService.getCurrentCluster().instanceName).subscribe(
      (notice: SubscriptionNoticeResponseDTO<MembersProductDTO>) => {
        this.data = notice.notice;
        this.tableModel.members = this.data;
      },
      (error: ErrorMessageDTO) => {
        this.snackbarService.show(`Could not fetch the members: ${error.errors}`);
      }
    );
  }

  public ngOnDestroy(): void {
    this.dataSub.unsubscribe();
  }

  public get clusterName(): string {
    return this.clustersService.getCurrentCluster().instanceName;
  }
}
