import {
  MdcButtonModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcListModule,
  MdcMenuModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {FormsModule} from '@angular/forms';
import {SharedScriptingButtonComponent} from '@shared/components/scripting-button/shared-scripting-button.component';
import {AceEditorModule} from 'ng2-ace-editor';
import {SharedHelpButtonModule} from '@shared/components/help-button/shared-help-button.module';

@NgModule({
  declarations: [
    SharedScriptingButtonComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AceEditorModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconModule,
    MdcButtonModule,
    MdcIconButtonModule,
    MdcListModule,
    MdcMenuModule,

    // Shared
    SharedHelpButtonModule,
    SharedServicesModule
  ],
  exports: [
    SharedScriptingButtonComponent
  ],
  providers: [
  ]
})
export class SharedScriptingButtonModule { }
