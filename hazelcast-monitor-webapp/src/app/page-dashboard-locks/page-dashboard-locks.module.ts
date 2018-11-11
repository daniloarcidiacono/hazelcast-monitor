import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {PageDashboardLocksComponent} from "./page-dashboard-locks.component";
import {MdcIconModule, MdcTypographyModule} from "@angular-mdc/web";
import {SharedMdcTableModule} from "@shared/components/mdc-table/shared-mdc-table.module";
import {SharedServicesModule} from "@shared/services/shared-services.module";

@NgModule({
  declarations: [
    PageDashboardLocksComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconModule,

    // Shared
    SharedMdcTableModule,
    SharedServicesModule
  ],
  providers: [
  ]
})

export class PageDashboardLocksModule {
}
