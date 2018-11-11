import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardRingbuffersComponent} from './page-dashboard-ringbuffers.component';
import {MdcDialogModule, MdcTypographyModule} from '@angular-mdc/web';
import {PageDashboardRingbuffersDialogComponent} from './page-dashboard-ringbuffers-dialog.component';

@NgModule({
  declarations: [
    PageDashboardRingbuffersComponent,
    PageDashboardRingbuffersDialogComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcDialogModule,

    // Shared
    SharedMdcTableModule,
    SharedServicesModule
  ],
  providers: [
  ],
  entryComponents: [
    PageDashboardRingbuffersDialogComponent
  ]
})
export class PageDashboardRingbuffersModule {
}
