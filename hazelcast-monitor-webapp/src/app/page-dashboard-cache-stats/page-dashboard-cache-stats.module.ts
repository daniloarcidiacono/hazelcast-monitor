import {NgModule} from '@angular/core';
import {PageDashboardCacheStatsComponent} from './page-dashboard-cache-stats.component';
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
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {DateFormatPipe, LocalTimePipe, MomentModule} from 'ngx-moment';
import {SharedSafeRoundPipe} from "@shared/pipes/shared-saferound.pipe";
import {SharedPipesModule} from "@shared/pipes/shared-pipes.module";
import {SharedFullscreenButtonModule} from "@shared/components/fullscreen-button/shared-fullscreen-button.module";

@NgModule({
  declarations: [
    PageDashboardCacheStatsComponent
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
    SharedFullscreenButtonModule,
    SharedMdcTableModule,
    SharedPipesModule,
    SharedServicesModule
  ],
  providers: [
    LocalTimePipe,
    DateFormatPipe,
    SharedSafeRoundPipe
  ]
})
export class PageDashboardCacheStatsModule {
}
