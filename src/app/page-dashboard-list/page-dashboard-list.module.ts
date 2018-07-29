import {NgModule} from '@angular/core';
import {PageDashboardListComponent} from './page-dashboard-list.component';
import {BrowserModule} from '@angular/platform-browser';
import {MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedServicesModule} from '@shared/services/shared-services.module';

@NgModule({
  declarations: [
    PageDashboardListComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcIconButtonModule,
    MdcIconModule,

    // Shared
    SharedMdcTableModule,
    SharedObjectInspectorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardListModule {
}
