import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardMapComponent} from './page-dashboard-map.component';
import {MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {PageDashboardMapTreeComponent} from "./page-dashboard-map-tree.component";
import {SharedMdcTreeModule} from "@shared/components/mdc-tree/shared-mdc-tree.module";

@NgModule({
  declarations: [
    PageDashboardMapComponent,
    PageDashboardMapTreeComponent
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
    SharedMdcTreeModule,
    SharedServicesModule
  ],
  providers: [
  ]
})
export class PageDashboardMapModule {
}
