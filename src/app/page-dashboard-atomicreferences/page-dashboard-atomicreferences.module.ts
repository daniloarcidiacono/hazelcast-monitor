import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardAtomicReferencesComponent} from './page-dashboard-atomicreferences.component';
import {MdcTypographyModule} from '@angular-mdc/web';
import {SharedObjectInspectorModule} from "@shared/components/object-inspector/shared-object-inspector.module";

@NgModule({
  declarations: [
    PageDashboardAtomicReferencesComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,

    // Shared
    SharedMdcTableModule,
    SharedObjectInspectorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardAtomicReferencesModule {
}
