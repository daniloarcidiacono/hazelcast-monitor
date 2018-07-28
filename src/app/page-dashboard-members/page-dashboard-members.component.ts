import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {MembersProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {Subscription} from 'rxjs/index';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {MembersTableModel} from './page-dashboard-members.model';
import {TabAwareComponent} from "@shared/components/dynamic-tabs/shared-dynamic-tabs.model";

@Component({
  templateUrl: './page-dashboard-members.component.html',
  styleUrls: [ './page-dashboard-members.component.scss' ]
})
export class PageDashboardMembersComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: MembersProductDTO = undefined;
  public tableModel: MembersTableModel = new MembersTableModel();

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public get clusterName(): string {
    return this.clustersService.getCurrentCluster().instanceName;
  }

  public beforeShow(): void {
    if (!this.dataSub) {
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
  }

  public beforeHide(): void {
    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
      this.dataSub = undefined;
    }
  }
}
