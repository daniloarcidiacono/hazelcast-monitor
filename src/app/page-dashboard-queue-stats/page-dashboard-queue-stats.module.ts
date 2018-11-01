import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcSelectModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {FormsModule} from '@angular/forms';
import {SharedHelpButtonModule} from '@shared/components/help-button/shared-help-button.module';
import {PageDashboardQueueStatsComponent} from './page-dashboard-queue-stats.component';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {DateFormatPipe, LocalTimePipe, MomentModule} from 'ngx-moment';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';
import {SharedOptionalValuePipe} from '@shared/pipes/shared-optionalvalue.pipe';

@NgModule({
  declarations: [
    PageDashboardQueueStatsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    MomentModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcSelectModule,
    MdcIconButtonModule,
    MdcIconModule,

    // Shared
    SharedHelpButtonModule,
    SharedMdcTableModule,
    SharedPipesModule,
    SharedServicesModule
  ],
  providers: [
    SharedOptionalValuePipe,
    LocalTimePipe,
    DateFormatPipe
  ]
})
export class PageDashboardQueueStatsModule {
}
