import {StorageServiceModule} from 'ngx-webstorage-service';
import {SharedWebStorageService} from './shared-webstorage.service';
import {SharedClusterGuard} from '../guards/shared-cluster.guard';
import {NgModule} from '@angular/core';
import {SharedClustersService} from './shared-clusters.service';
import {SharedWebSocketService} from './shared-websocket.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedDashboardGuard} from '@shared/guards/shared-dashboard-guard';
import {SharedTabsService} from "@shared/services/shared-tabs.service";

@NgModule({
  declarations: [
  ],
  imports: [
    StorageServiceModule
  ],
  exports: [
  ],
  providers: [
    SharedWebStorageService,
    SharedClustersService,
    SharedDashboardGuard,
    SharedClusterGuard,
    SharedWebSocketService,
    SharedSnackbarService,
    SharedTabsService,
    SharedHazelcastAgentService
  ]
})
export class SharedServicesModule {
}
