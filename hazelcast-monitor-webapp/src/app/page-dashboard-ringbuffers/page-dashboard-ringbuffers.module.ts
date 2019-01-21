import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {PageDashboardRingbuffersComponent} from './page-dashboard-ringbuffers.component';
import {MdcDialogModule, MdcIconButtonModule, MdcTextFieldModule, MdcTypographyModule} from '@angular-mdc/web';
import {PageDashboardRingbuffersDialogComponent} from './page-dashboard-ringbuffers-dialog.component';
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    PageDashboardRingbuffersComponent,
    PageDashboardRingbuffersDialogComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcDialogModule,
    MdcIconButtonModule,
    MdcTextFieldModule,

    // Shared
    SharedMdcTableModule,
    SharedMdcPaginatorModule,
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
