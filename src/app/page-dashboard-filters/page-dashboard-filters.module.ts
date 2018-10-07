import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule,
  MdcCheckboxModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcListModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {FormsModule} from '@angular/forms';
import {PageDashboardFiltersComponent} from './page-dashboard-filters.component';
import {SharedFiltersEditorModule} from "@shared/components/filters-editor/shared-filters-editor.module";

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

    // Shared
    SharedFiltersEditorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardFiltersModule {
}
