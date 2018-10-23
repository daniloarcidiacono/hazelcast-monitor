import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardReplicatedMapComponent} from './page-dashboard-replicatedmap.component';
import {MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedScriptingButtonModule} from '@shared/components/scripting-button/shared-scripting-button.module';
import {SharedFrequencyButtonModule} from '@shared/components/frequency-button/shared-frequency-button.module';
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';

@NgModule({
  declarations: [
    PageDashboardReplicatedMapComponent
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
    SharedMdcPaginatorModule,
    SharedFrequencyButtonModule,
    SharedScriptingButtonModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardReplicatedMapModule {
}
