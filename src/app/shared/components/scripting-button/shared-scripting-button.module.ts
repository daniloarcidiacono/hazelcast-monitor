import {
  MdcButtonModule,
  MdcDialogModule,
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
import {SharedScriptingButtonHelpDialog} from '@shared/components/scripting-button/shared-scripting-button-help.dialog';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';

@NgModule({
  declarations: [
    SharedScriptingButtonComponent,
    SharedScriptingButtonHelpDialog
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AceEditorModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconModule,
    MdcButtonModule,
    MdcListModule,
    MdcMenuModule,
    MdcDialogModule,

    // Shared
    SharedServicesModule,
    SharedPipesModule
  ],
  exports: [
    SharedScriptingButtonComponent
  ],
  providers: [
  ],
  entryComponents: [
    SharedScriptingButtonHelpDialog
  ]
})
export class SharedScriptingButtonModule { }
