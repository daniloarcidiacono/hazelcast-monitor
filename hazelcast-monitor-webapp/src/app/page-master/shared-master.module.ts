import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {MdcDialogModule, MdcIconButtonModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedSearchBarModule} from '@shared/components/search-bar/shared-search-bar.module';
import {SharedMasterFooterComponent} from './master-footer/shared-master-footer.component';
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';
import {SharedMasterHeaderComponent} from './master-header/shared-master-header.component';
import {PageDashboardAtomicLongsComponent} from './master-atomiclongs/page-dashboard-atomiclongs.component';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardTopicsComponent} from './master-topics/page-dashboard-topics.component';
import {PageDashboardAtomicReferencesComponent} from "./master-atomicreferences/page-dashboard-atomicreferences.component";
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {PageDashboardCachesComponent} from "./master-caches/page-dashboard-caches.component";
import {PageDashboardCardinalityEstimatorsComponent} from "./master-cardinalityestimators/page-dashboard-cardinalityestimators.component";
import {PageDashboardCountdownLatchesComponent} from "./master-countdownlatches/page-dashboard-countdownlatches.component";
import {PageDashboardExecutorsComponent} from "./master-executors/page-dashboard-executors.component";
import {PageDashboardListsComponent} from "./master-lists/page-dashboard-lists.component";
import {PageDashboardLocksComponent} from "./master-locks/page-dashboard-locks.component";
import {PageDashboardMapsComponent} from "./master-maps/page-dashboard-maps.component";
import {PageDashboardMultiMapsComponent} from "./master-multimaps/page-dashboard-multimaps.component";
import {PageDashboardQueuesComponent} from "./master-queues/page-dashboard-queues.component";
import {PageDashboardReplicatedMapsComponent} from "./master-replicatedmaps/page-dashboard-replicatedmaps.component";
import {PageDashboardRingbuffersComponent} from "./master-ringbuffers/page-dashboard-ringbuffers.component";
import {PageDashboardSemaphoresComponent} from "./master-semaphores/page-dashboard-semaphores.component";
import {PageDashboardSetsComponent} from "./master-sets/page-dashboard-sets.component";
import {PageDashboardRingbuffersDialogComponent} from "./master-ringbuffers/page-dashboard-ringbuffers-dialog.component";

@NgModule({
  declarations: [
    SharedMasterHeaderComponent,
    SharedMasterFooterComponent,

    PageDashboardAtomicLongsComponent,
    PageDashboardAtomicReferencesComponent,
    PageDashboardCachesComponent,
    PageDashboardCardinalityEstimatorsComponent,
    PageDashboardCountdownLatchesComponent,
    PageDashboardExecutorsComponent,
    PageDashboardListsComponent,
    PageDashboardLocksComponent,
    PageDashboardMapsComponent,
    PageDashboardMultiMapsComponent,
    PageDashboardQueuesComponent,
    PageDashboardReplicatedMapsComponent,
    PageDashboardRingbuffersComponent,
    PageDashboardRingbuffersDialogComponent,
    PageDashboardSemaphoresComponent,
    PageDashboardSetsComponent,
    PageDashboardTopicsComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    // Angular MDC Web
    MdcTypographyModule,
    MdcDialogModule,
    MdcIconModule,
    MdcIconButtonModule,
    SharedSearchBarModule,

    // Shared
    SharedMdcPaginatorModule,
    SharedObjectInspectorModule,
    SharedMdcTableModule
  ],
  exports: [
    SharedMasterHeaderComponent,
    SharedMasterFooterComponent,

    PageDashboardAtomicLongsComponent,
    PageDashboardAtomicReferencesComponent,
    PageDashboardCachesComponent,
    PageDashboardCardinalityEstimatorsComponent,
    PageDashboardCountdownLatchesComponent,
    PageDashboardExecutorsComponent,
    PageDashboardListsComponent,
    PageDashboardLocksComponent,
    PageDashboardMapsComponent,
    PageDashboardMultiMapsComponent,
    PageDashboardQueuesComponent,
    PageDashboardReplicatedMapsComponent,
    PageDashboardRingbuffersComponent,
    PageDashboardRingbuffersDialogComponent,
    PageDashboardSemaphoresComponent,
    PageDashboardSetsComponent,
    PageDashboardTopicsComponent
  ],
  entryComponents: [
    PageDashboardAtomicLongsComponent,
    PageDashboardAtomicReferencesComponent,
    PageDashboardCachesComponent,
    PageDashboardCardinalityEstimatorsComponent,
    PageDashboardCountdownLatchesComponent,
    PageDashboardExecutorsComponent,
    PageDashboardListsComponent,
    PageDashboardLocksComponent,
    PageDashboardMapsComponent,
    PageDashboardMultiMapsComponent,
    PageDashboardQueuesComponent,
    PageDashboardReplicatedMapsComponent,
    PageDashboardRingbuffersComponent,
    PageDashboardRingbuffersDialogComponent,
    PageDashboardSemaphoresComponent,
    PageDashboardSetsComponent,
    PageDashboardTopicsComponent
  ]
})
export class SharedMasterModule { }
