import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageDashboardComponent} from './page-dashboard.component';
import {
  MdcDrawerModule,
  MdcElevationModule,
  MdcIconModule,
  MdcListModule,
  MdcRippleModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {PageDashboardMembersModule} from '../page-dashboard-members/page-dashboard-members.module';
// import {PageDashboardRoutingModule} from './page-dashboard-routing.module';
import {RouterModule} from '@angular/router';
import {PageDashboardMapModule} from '../page-dashboard-map/page-dashboard-map.module';
import {SharedDynamicTabsModule} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.module';
import {PageDashboardMembersComponent} from '../page-dashboard-members/page-dashboard-members.component';
import {PageDashboardMapComponent} from '../page-dashboard-map/page-dashboard-map.component';
import {PageDashboardListComponent} from '../page-dashboard-list/page-dashboard-list.component';
import {PageDashboardListModule} from '../page-dashboard-list/page-dashboard-list.module';
import {PageDashboardMultiMapComponent} from '../page-dashboard-multimap/page-dashboard-multimap.component';
import {PageDashboardMultiMapModule} from '../page-dashboard-multimap/page-dashboard-multimap.module';
import {PageDashboardReplicatedMapModule} from '../page-dashboard-replicatedmap/page-dashboard-replicatedmap.module';
import {PageDashboardReplicatedMapComponent} from '../page-dashboard-replicatedmap/page-dashboard-replicatedmap.component';
import {PageDashboardSetModule} from '../page-dashboard-set/page-dashboard-set.module';
import {PageDashboardSetComponent} from '../page-dashboard-set/page-dashboard-set.component';
import {PageDashboardQueueComponent} from '../page-dashboard-queue/page-dashboard-queue.component';
import {PageDashboardQueueModule} from '../page-dashboard-queue/page-dashboard-queue.module';
import {PageDashboardTopicComponent} from '../page-dashboard-topic/page-dashboard-topic.component';
import {PageDashboardTopicModule} from '../page-dashboard-topic/page-dashboard-topic.module';
import {PageDashboardCacheComponent} from '../page-dashboard-cache/page-dashboard-cache.component';
import {PageDashboardCacheModule} from '../page-dashboard-cache/page-dashboard-cache.module';
import {PageDashboardInternalsComponent} from '../page-dashboard-internals/page-dashboard-internals.component';
import {PageDashboardInternalsModule} from '../page-dashboard-internals/page-dashboard-internals.module';
import {SectionCardComponent} from './section-card.component';
import {StatisticsGaugeModule} from './statistics-gauge.module';
import {PageDashboardTopicStatsModule} from "../page-dashboard-topic-stats/page-dashboard-topic-stats.module";
import {PageDashboardTopicStatsComponent} from "../page-dashboard-topic-stats/page-dashboard-topic-stats.component";
import {PageDashboardQueueStatsComponent} from "../page-dashboard-queue-stats/page-dashboard-queue-stats.component";
import {PageDashboardQueueStatsModule} from "../page-dashboard-queue-stats/page-dashboard-queue-stats.module";
import {PageDashboardExecutorStatsComponent} from "../page-dashboard-executor-stats/page-dashboard-executor-stats.component";
import {PageDashboardExecutorStatsModule} from "../page-dashboard-executor-stats/page-dashboard-executor-stats.module";
import {PageDashboardCacheStatsModule} from "../page-dashboard-cache-stats/page-dashboard-cache-stats.module";
import {PageDashboardCacheStatsComponent} from "../page-dashboard-cache-stats/page-dashboard-cache-stats.component";
import {PageDashboardMapStatsComponent} from "../page-dashboard-map-stats/page-dashboard-map-stats.component";
import {PageDashboardMapStatsModule} from "../page-dashboard-map-stats/page-dashboard-map-stats.module";
import {SharedMasterModule} from "../page-master/shared-master.module";

@NgModule({
  declarations: [
    PageDashboardComponent,
    SectionCardComponent
  ],
  imports: [
    BrowserModule,

    // Sub-sections
    SharedMasterModule,
    PageDashboardMembersModule,
    PageDashboardExecutorStatsModule,
    PageDashboardInternalsModule,
    PageDashboardMapModule,
    PageDashboardMapStatsModule,
    PageDashboardListModule,
    PageDashboardMultiMapModule,
    PageDashboardReplicatedMapModule,
    PageDashboardSetModule,
    PageDashboardQueueModule,
    PageDashboardQueueStatsModule,
    PageDashboardTopicModule,
    PageDashboardTopicStatsModule,
    PageDashboardCacheModule,
    PageDashboardCacheStatsModule,

    // Local modules
    StatisticsGaugeModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcDrawerModule,
    MdcListModule,
    MdcIconModule,
    MdcElevationModule,
    MdcRippleModule,

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
    PageDashboardExecutorStatsComponent,
    PageDashboardInternalsComponent,
    PageDashboardMapComponent,
    PageDashboardMapStatsComponent,
    PageDashboardMultiMapComponent,
    PageDashboardReplicatedMapComponent,
    PageDashboardListComponent,
    PageDashboardSetComponent,
    PageDashboardQueueComponent,
    PageDashboardQueueStatsComponent,
    PageDashboardTopicComponent,
    PageDashboardTopicStatsComponent,
    PageDashboardCacheComponent,
    PageDashboardCacheStatsComponent
  ]
})
export class PageDashboardModule { }
