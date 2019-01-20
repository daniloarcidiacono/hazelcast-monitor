import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {MdcIconModule} from '@angular-mdc/web';
import {SharedFullscreenButtonComponent} from "@shared/components/fullscreen-button/shared-fullscreen-button.component";

@NgModule({
  declarations: [
    SharedFullscreenButtonComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcIconModule
  ],
  exports: [
    SharedFullscreenButtonComponent
  ]
})
export class SharedFullscreenButtonModule { }
