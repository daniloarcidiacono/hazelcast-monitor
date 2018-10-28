import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {MdcDialogModule, MdcIconModule} from '@angular-mdc/web';
import {SharedHelpButtonComponent} from '@shared/components/help-button/shared-help-button.component';
import {SharedHelpButtonDialog} from '@shared/components/help-button/shared-help-button.dialog';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';

@NgModule({
  declarations: [
    SharedHelpButtonComponent,
    SharedHelpButtonDialog
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcIconModule,
    MdcDialogModule
  ],
  exports: [
    SharedHelpButtonComponent
  ],
  entryComponents: [
    SharedHelpButtonDialog
  ]
})
export class SharedHelpButtonModule { }
