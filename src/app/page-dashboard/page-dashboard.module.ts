import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageDashboardComponent} from './page-dashboard.component';
import {MdcButtonModule, MdcDrawerModule, MdcFabModule, MdcIconModule, MdcListModule} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';

@NgModule({
  declarations: [
    PageDashboardComponent
  ],
  imports: [
    BrowserModule,
    MdcListModule,
    MdcDrawerModule,
    MdcButtonModule,
    MdcFabModule,
    MdcIconModule
  ],
  providers: [
    SharedServicesModule
  ]
})
export class PageDashboardModule { }
