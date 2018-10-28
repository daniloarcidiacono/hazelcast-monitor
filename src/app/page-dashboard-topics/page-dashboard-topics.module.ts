import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardTopicsComponent} from './page-dashboard-topics.component';
import {MdcIconButtonModule, MdcTypographyModule} from '@angular-mdc/web';

@NgModule({
  declarations: [
    PageDashboardTopicsComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconButtonModule,

    // Shared
    SharedMdcTableModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardTopicsModule {
}
