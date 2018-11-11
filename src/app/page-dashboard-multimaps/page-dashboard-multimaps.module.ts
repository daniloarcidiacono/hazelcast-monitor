import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {MdcIconButtonModule, MdcTextFieldModule, MdcTypographyModule} from '@angular-mdc/web';
import {PageDashboardMultiMapsComponent} from './page-dashboard-multimaps.component';
import {FormsModule} from '@angular/forms';
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';

@NgModule({
  declarations: [
    PageDashboardMultiMapsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconButtonModule,
    MdcTextFieldModule,

    // Shared
    SharedMdcTableModule,
    SharedMdcPaginatorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardMultiMapsModule {
}
