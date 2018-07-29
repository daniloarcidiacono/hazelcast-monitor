import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {MdcTypographyModule} from '@angular-mdc/web';
import {PageDashboardMultiMapsComponent} from "./page-dashboard-multimaps.component";

@NgModule({
  declarations: [
    PageDashboardMultiMapsComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,

    // Shared
    SharedMdcTableModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardMultiMapsModule {
}
