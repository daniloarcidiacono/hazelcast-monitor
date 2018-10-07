import {NgModule} from '@angular/core';
import {PageDashboardListComponent} from './page-dashboard-list.component';
import {BrowserModule} from '@angular/platform-browser';
import {MdcButtonModule, MdcDialogModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedFiltersBarModule} from "@shared/components/filters-bar/shared-filters-bar.module";

@NgModule({
  declarations: [
    PageDashboardListComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcIconModule,
    MdcDialogModule,

    // Shared
    SharedMdcTableModule,
    SharedObjectInspectorModule,
    SharedFiltersBarModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardListModule {
}
