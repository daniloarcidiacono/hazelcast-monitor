import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {
  MdcButtonModule, MdcCheckboxModule,
  MdcElevationModule,
  MdcFormFieldModule,
  MdcIconModule, MdcLinearProgressModule, MdcSelectModule,
  MdcTextFieldModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {PageConnectComponent} from './page-connect.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    PageConnectComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,

    // Angular MDC Web
    MdcFormFieldModule,
    MdcTextFieldModule,
    MdcSelectModule,
    MdcElevationModule,
    MdcLinearProgressModule,
    MdcCheckboxModule,
    MdcIconModule,
    MdcButtonModule,
    MdcTypographyModule
  ],
  providers: [
    SharedServicesModule
  ]
})
export class PageConnectModule { }
