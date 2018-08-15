import {NgModule} from '@angular/core';
import {PageDashboardTopicComponent} from './page-dashboard-topic.component';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcSelectModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {MomentModule} from 'ngx-moment';
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    PageDashboardTopicComponent
  ],
  imports: [
    BrowserModule,
    MomentModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcSelectModule,
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
export class PageDashboardTopicModule {
}
