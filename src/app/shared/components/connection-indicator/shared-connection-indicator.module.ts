import {SharedConnectionIndicatorComponent} from '@shared/components/connection-indicator/shared-connection-indicator.component';
import {MdcTypographyModule} from '@angular-mdc/web';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedServicesModule} from '@shared/services/shared-services.module';

@NgModule({
  declarations: [
    SharedConnectionIndicatorComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule
  ],
  exports: [
    SharedConnectionIndicatorComponent
  ],
  providers: [
    SharedServicesModule
  ]
})
export class SharedConnectionIndicatorModule { }
