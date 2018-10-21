import {NgModule} from '@angular/core';
import {PageDashboardInternalsComponent} from './page-dashboard-internals.component';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule, MdcCardModule, MdcIconButtonModule, MdcIconModule, MdcListModule, MdcRippleModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {AceEditorModule} from 'ng2-ace-editor';

@NgModule({
  declarations: [
    PageDashboardInternalsComponent
  ],
  imports: [
    BrowserModule,
    AceEditorModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcIconButtonModule,
    MdcIconModule,
    MdcCardModule,
    MdcListModule,
    MdcRippleModule,

    // Shared
    SharedMdcTableModule,
    SharedObjectInspectorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardInternalsModule {
}
