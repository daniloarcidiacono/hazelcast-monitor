import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {RingbuffersProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {RingbufferSummaryDTO} from '@shared/dto/topic-products.dto';
import {PageDashboardRingbuffersDialogComponent} from './page-dashboard-ringbuffers-dialog.component';
import {MdcDialog} from '@angular-mdc/web';

@Component({
  templateUrl: './page-dashboard-ringbuffers.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardRingbuffersComponent extends SharedMasterComponent<RingbuffersProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private dialog: MdcDialog) {
    super(DistributedObjectType.RINGBUFFER, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Ringbuffers of ${this.clusterName}`;
  }

  public navigateToDetails(row: RingbufferSummaryDTO): void {
    const ringbufferName: string = row.name;
    this.dialog.open(PageDashboardRingbuffersDialogComponent, {
      data: {
        ringbufferName: ringbufferName
      },
      escapeToClose: true,
      clickOutsideToClose: true
    });
  }
}
