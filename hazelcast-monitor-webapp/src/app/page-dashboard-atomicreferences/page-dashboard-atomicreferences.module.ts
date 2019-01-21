import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardAtomicReferencesComponent} from './page-dashboard-atomicreferences.component';
import {MdcIconButtonModule, MdcTextFieldModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedObjectInspectorModule} from "@shared/components/object-inspector/shared-object-inspector.module";
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    PageDashboardAtomicReferencesComponent
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
    SharedObjectInspectorModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardAtomicReferencesModule {
}
