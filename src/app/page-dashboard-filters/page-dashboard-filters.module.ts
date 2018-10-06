import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule, MdcCheckboxModule, MdcIconButtonModule, MdcIconModule, MdcListModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {MonacoEditorModule} from 'ngx-monaco-editor';
import {FormsModule} from '@angular/forms';
import {PageDashboardFiltersComponent} from './page-dashboard-filters.component';

@NgModule({
  declarations: [
    PageDashboardFiltersComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcListModule,
    MdcCheckboxModule,
    MdcIconButtonModule,
    MdcIconModule,
    MonacoEditorModule,

    // Shared
    SharedMdcTableModule,
    SharedObjectInspectorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardFiltersModule {
}
