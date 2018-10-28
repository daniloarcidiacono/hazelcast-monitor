import {NgModule} from '@angular/core';
import {PageDashboardTopicStatsComponent} from './page-dashboard-topic-stats.component';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule,
  MdcElevationModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcSelectModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {FormsModule} from '@angular/forms';
import {SharedHelpButtonModule} from "@shared/components/help-button/shared-help-button.module";

@NgModule({
  declarations: [
    PageDashboardTopicStatsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcSelectModule,
    MdcIconButtonModule,
    MdcIconModule,

    // Shared
    SharedHelpButtonModule,
    SharedServicesModule
  ]
})
export class PageDashboardTopicStatsModule {
}
