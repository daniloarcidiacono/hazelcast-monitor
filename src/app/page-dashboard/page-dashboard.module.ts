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
import {PageDashboardListModule} from "../page-dashboard-list/page-dashboard-list.module";

@NgModule({
  declarations: [
    PageDashboardComponent
  ],
  imports: [
    BrowserModule,

    // Sub-sections
    PageDashboardMembersModule,
    PageDashboardMapsModule,
    PageDashboardListsModule,
    PageDashboardLocksModule,
    PageDashboardMapModule,
    PageDashboardListModule,

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
    PageDashboardLocksComponent,
    PageDashboardListsComponent,
    PageDashboardMapsComponent,
    PageDashboardMapComponent,
    PageDashboardListComponent
  ]
})
export class PageDashboardModule { }
