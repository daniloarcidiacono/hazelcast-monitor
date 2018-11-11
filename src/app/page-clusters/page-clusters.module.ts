import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageClustersComponent} from './page-clusters.component';
import {
  MdcButtonModule,
  MdcElevationModule,
  MdcFormFieldModule,
  MdcIconModule,
  MdcSelectModule,
  MdcTextFieldModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    PageClustersComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,

    // Angular MDC Web
    MdcFormFieldModule,
    MdcTextFieldModule,
    MdcSelectModule,
    MdcElevationModule,
    MdcIconModule,
    MdcButtonModule,
    MdcTypographyModule
  ],
  providers: [
    SharedServicesModule
  ]
})
export class PageClustersModule { }
