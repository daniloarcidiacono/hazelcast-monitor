import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageDashboardComponent} from './page-dashboard.component';
import {MdcDrawerModule, MdcIconModule, MdcListModule} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {PageDashboardMembersModule} from '../page-dashboard-members/page-dashboard-members.module';
// import {PageDashboardRoutingModule} from './page-dashboard-routing.module';
import {RouterModule} from '@angular/router';
import {PageDashboardMapsModule} from "../page-dashboard-maps/page-dashboard-maps.module";
import {PageDashboardMapModule} from "../page-dashboard-map/page-dashboard-map.module";
import {SharedDynamicTabsModule} from "@shared/components/dynamic-tabs/shared-dynamic-tabs.module";

@NgModule({
  declarations: [
    PageDashboardComponent
  ],
  imports: [
    BrowserModule,

    // Sub-sections
    PageDashboardMembersModule,
    PageDashboardMapsModule,
    PageDashboardMapModule,

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
  ]
})
export class PageDashboardModule { }
