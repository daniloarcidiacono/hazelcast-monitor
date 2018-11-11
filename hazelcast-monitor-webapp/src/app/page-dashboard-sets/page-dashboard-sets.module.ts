import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardSetsComponent} from './page-dashboard-sets.component';
import {MdcTypographyModule} from '@angular-mdc/web';

@NgModule({
  declarations: [
    PageDashboardSetsComponent
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
export class PageDashboardSetsModule {
}
