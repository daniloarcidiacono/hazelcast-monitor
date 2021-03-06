import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from "@shared/components/mdc-table/shared-mdc-table.module";
import {PageDashboardMembersComponent} from './page-dashboard-members.component';
import {MdcTypographyModule} from "@angular-mdc/web";

@NgModule({
  declarations: [
    PageDashboardMembersComponent
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
export class PageDashboardMembersModule {
}
