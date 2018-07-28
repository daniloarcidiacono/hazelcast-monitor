import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from "@shared/components/mdc-table/shared-mdc-table.module";
import {PageDashboardMapsComponent} from './page-dashboard-maps.component';
import {MdcTypographyModule} from "@angular-mdc/web";

@NgModule({
  declarations: [
    PageDashboardMapsComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,

    // Shared
    SharedServicesModule,
    SharedMdcTableModule
  ],
  providers: [
  ]
})
export class PageDashboardMapsModule {
}
