import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageDashboardComponent} from './page-dashboard.component';
import {MdcDrawerModule, MdcIconModule, MdcListModule} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {PageDashboardMembersModule} from '../page-dashboard-members/page-dashboard-members.module';
// import {PageDashboardRoutingModule} from './page-dashboard-routing.module';
import {RouterModule} from '@angular/router';
import {PageDashboardMapsModule} from '../page-dashboard-maps/page-dashboard-maps.module';
import {PageDashboardMapModule} from '../page-dashboard-map/page-dashboard-map.module';
import {SharedDynamicTabsModule} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.module';
import {PageDashboardLocksModule} from '../page-dashboard-locks/page-dashboard-locks.module';
import {PageDashboardLocksComponent} from '../page-dashboard-locks/page-dashboard-locks.component';
import {PageDashboardMapsComponent} from '../page-dashboard-maps/page-dashboard-maps.component';
import {PageDashboardMembersComponent} from '../page-dashboard-members/page-dashboard-members.component';
import {PageDashboardMapComponent} from '../page-dashboard-map/page-dashboard-map.component';
import {PageDashboardListsModule} from '../page-dashboard-lists/page-dashboard-lists.module';
import {PageDashboardListsComponent} from '../page-dashboard-lists/page-dashboard-lists.component';
import {PageDashboardListComponent} from '../page-dashboard-list/page-dashboard-list.component';
import {PageDashboardListModule} from '../page-dashboard-list/page-dashboard-list.module';
import {PageDashboardMultiMapsModule} from '../page-dashboard-multimaps/page-dashboard-multimaps.module';
import {PageDashboardMultiMapsComponent} from '../page-dashboard-multimaps/page-dashboard-multimaps.component';
import {PageDashboardMultiMapComponent} from '../page-dashboard-multimap/page-dashboard-multimap.component';
import {PageDashboardMultiMapModule} from '../page-dashboard-multimap/page-dashboard-multimap.module';
import {PageDashboardReplicatedMapsModule} from '../page-dashboard-replicatedmaps/page-dashboard-replicatedmaps.module';
import {PageDashboardReplicatedMapModule} from '../page-dashboard-replicatedmap/page-dashboard-replicatedmap.module';
import {PageDashboardReplicatedMapsComponent} from '../page-dashboard-replicatedmaps/page-dashboard-replicatedmaps.component';
import {PageDashboardReplicatedMapComponent} from '../page-dashboard-replicatedmap/page-dashboard-replicatedmap.component';
import {PageDashboardSetsModule} from '../page-dashboard-sets/page-dashboard-sets.module';
import {PageDashboardSetModule} from '../page-dashboard-set/page-dashboard-set.module';
import {PageDashboardSetsComponent} from '../page-dashboard-sets/page-dashboard-sets.component';
import {PageDashboardSetComponent} from '../page-dashboard-set/page-dashboard-set.component';
import {PageDashboardQueuesComponent} from '../page-dashboard-queues/page-dashboard-queues.component';
import {PageDashboardQueueComponent} from '../page-dashboard-queue/page-dashboard-queue.component';
import {PageDashboardQueuesModule} from '../page-dashboard-queues/page-dashboard-queues.module';
import {PageDashboardQueueModule} from '../page-dashboard-queue/page-dashboard-queue.module';
import {PageDashboardTopicsModule} from '../page-dashboard-topics/page-dashboard-topics.module';
import {PageDashboardTopicsComponent} from '../page-dashboard-topics/page-dashboard-topics.component';
import {PageDashboardTopicComponent} from '../page-dashboard-topic/page-dashboard-topic.component';
import {PageDashboardTopicModule} from '../page-dashboard-topic/page-dashboard-topic.module';
import {PageDashboardAtomicLongsModule} from '../page-dashboard-atomiclongs/page-dashboard-atomiclongs.module';
import {PageDashboardAtomicLongsComponent} from '../page-dashboard-atomiclongs/page-dashboard-atomiclongs.component';
import {PageDashboardAtomicReferencesModule} from '../page-dashboard-atomicreferences/page-dashboard-atomicreferences.module';
import {PageDashboardAtomicReferencesComponent} from '../page-dashboard-atomicreferences/page-dashboard-atomicreferences.component';
import {PageDashboardCountdownLatchesModule} from '../page-dashboard-countdownlatches/page-dashboard-countdownlatches.module';
import {PageDashboardCountdownLatchesComponent} from '../page-dashboard-countdownlatches/page-dashboard-countdownlatches.component';
import {PageDashboardSemaphoresModule} from '../page-dashboard-semaphores/page-dashboard-semaphores.module';
import {PageDashboardSemaphoresComponent} from '../page-dashboard-semaphores/page-dashboard-semaphores.component';
import {PageDashboardCachesComponent} from '../page-dashboard-caches/page-dashboard-caches.component';
import {PageDashboardCacheComponent} from '../page-dashboard-cache/page-dashboard-cache.component';
import {PageDashboardCachesModule} from '../page-dashboard-caches/page-dashboard-caches.module';
import {PageDashboardCacheModule} from '../page-dashboard-cache/page-dashboard-cache.module';
import {PageDashboardRingbuffersModule} from '../page-dashboard-ringbuffers/page-dashboard-ringbuffers.module';
import {PageDashboardRingbuffersComponent} from '../page-dashboard-ringbuffers/page-dashboard-ringbuffers.component';
import {PageDashboardInternalsComponent} from '../page-dashboard-internals/page-dashboard-internals.component';
import {PageDashboardInternalsModule} from '../page-dashboard-internals/page-dashboard-internals.module';
import {PageDashboardFiltersModule} from '../page-dashboard-filters/page-dashboard-filters.module';
import {PageDashboardFiltersComponent} from '../page-dashboard-filters/page-dashboard-filters.component';

@NgModule({
  declarations: [
    PageDashboardComponent
  ],
  imports: [
    BrowserModule,

    // Sub-sections
    PageDashboardMembersModule,
    PageDashboardFiltersModule,
    PageDashboardMapsModule,
    PageDashboardListsModule,
    PageDashboardLocksModule,
    PageDashboardMultiMapsModule,
    PageDashboardSetsModule,
    PageDashboardReplicatedMapsModule,
    PageDashboardQueuesModule,
    PageDashboardTopicsModule,
    PageDashboardAtomicLongsModule,
    PageDashboardAtomicReferencesModule,
    PageDashboardCountdownLatchesModule,
    PageDashboardSemaphoresModule,
    PageDashboardRingbuffersModule,
    PageDashboardCachesModule,
    PageDashboardInternalsModule,
    PageDashboardMapModule,
    PageDashboardListModule,
    PageDashboardMultiMapModule,
    PageDashboardReplicatedMapModule,
    PageDashboardSetModule,
    PageDashboardQueueModule,
    PageDashboardTopicModule,
    PageDashboardCacheModule,

    // Angular MDC Web
    MdcDrawerModule,
    MdcListModule,
    MdcIconModule,

    // Shared
    SharedServicesModule,
    SharedDynamicTabsModule,

    // Routing
    RouterModule,
    // PageDashboardRoutingModule
  ],
  providers: [
  ],
  entryComponents: [
    PageDashboardMembersComponent,
    PageDashboardFiltersComponent,
    PageDashboardLocksComponent,
    PageDashboardListsComponent,
    PageDashboardMapsComponent,
    PageDashboardMultiMapsComponent,
    PageDashboardReplicatedMapsComponent,
    PageDashboardTopicsComponent,
    PageDashboardSetsComponent,
    PageDashboardQueuesComponent,
    PageDashboardAtomicLongsComponent,
    PageDashboardAtomicReferencesComponent,
    PageDashboardCountdownLatchesComponent,
    PageDashboardSemaphoresComponent,
    PageDashboardRingbuffersComponent,
    PageDashboardCachesComponent,
    PageDashboardInternalsComponent,
    PageDashboardMapComponent,
    PageDashboardListComponent,
    PageDashboardMultiMapComponent,
    PageDashboardReplicatedMapComponent,
    PageDashboardSetComponent,
    PageDashboardQueueComponent,
    PageDashboardTopicComponent,
    PageDashboardCacheComponent
  ]
})
export class PageDashboardModule { }
