import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardCacheComponent} from './page-dashboard-cache.component';
import {MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';
import {SharedFrequencyButtonModule} from '@shared/components/frequency-button/shared-frequency-button.module';
import {SharedScriptingButtonModule} from '@shared/components/scripting-button/shared-scripting-button.module';

@NgModule({
  declarations: [
    PageDashboardCacheComponent
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
export class PageDashboardCacheModule {
}
