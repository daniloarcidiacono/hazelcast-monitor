import {
  MdcButtonModule, MdcDialogModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcSelectModule,
  MdcTextFieldModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {SharedFiltersBarComponent} from '@shared/components/filters-bar/shared-filters-bar.component';
import {AceEditorModule} from 'ng2-ace-editor';
import {FormsModule} from '@angular/forms';
import {SharedFiltersBarHelpDialog} from "@shared/components/filters-bar/shared-filters-bar-help.dialog";

@NgModule({
  declarations: [
    SharedFiltersBarComponent,
    SharedFiltersBarHelpDialog
  ],
  imports: [
    BrowserModule,
    FormsModule,

    AceEditorModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconButtonModule,
    MdcIconModule,
    MdcButtonModule,
    MdcTextFieldModule,
    MdcIconButtonModule,
    MdcSelectModule,
    MdcDialogModule
  ],
  exports: [
    SharedFiltersBarComponent
  ],
  providers: [
    SharedServicesModule
  ],
  entryComponents: [
    SharedFiltersBarHelpDialog
  ]
})
export class SharedFiltersBarModule { }
