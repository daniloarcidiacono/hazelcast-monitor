import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageDashboardComponent} from './page-dashboard.component';
import {MdcButtonModule, MdcDrawerModule, MdcFabModule, MdcIconModule, MdcListModule} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {PageDashboardMembersModule} from '../page-dashboard-members/page-dashboard-members.module';
// import {PageDashboardRoutingModule} from './page-dashboard-routing.module';
import {RouterModule} from '@angular/router';
import {PageDashboardMapsModule} from "../page-dashboard-maps/page-dashboard-maps.module";
import {PageDashboardMapModule} from "../page-dashboard-map/page-dashboard-map.module";

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
    MdcListModule,
    MdcDrawerModule,
    MdcButtonModule,
    MdcFabModule,
    MdcIconModule,

    // Routing
    RouterModule,
    // PageDashboardRoutingModule
  ],
  providers: [
    SharedServicesModule
  ]
})
export class PageDashboardModule { }
