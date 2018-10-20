import {MdcButtonModule, MdcIconModule, MdcListModule, MdcMenuModule, MdcTypographyModule} from '@angular-mdc/web';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {FormsModule} from '@angular/forms';
import {SharedFrequencyButtonComponent} from '@shared/components/frequency-button/shared-frequency-button.component';

@NgModule({
  declarations: [
    SharedFrequencyButtonComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconModule,
    MdcButtonModule,
    MdcListModule,
    MdcMenuModule,

    // Shared
    SharedServicesModule
  ],
  exports: [
    SharedFrequencyButtonComponent
  ],
  providers: [
  ]
})
export class SharedFrequencyButtonModule { }
