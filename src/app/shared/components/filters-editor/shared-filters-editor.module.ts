import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {FormsModule} from '@angular/forms';
import {SharedFiltersEditorComponent} from '@shared/components/filters-editor/shared-filters-editor.component';
import {MonacoEditorModule} from 'ngx-monaco-editor';
import {MdcButtonModule, MdcCheckboxModule, MdcIconButtonModule, MdcIconModule, MdcListModule} from "@angular-mdc/web";

@NgModule({
  declarations: [
    SharedFiltersEditorComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    MonacoEditorModule,

    // Angular MDC Web
    MdcButtonModule,
    MdcListModule,
    MdcCheckboxModule,
    MdcIconButtonModule,
    MdcIconModule
  ],
  exports: [
    SharedFiltersEditorComponent
  ],
  providers: [
    SharedServicesModule
  ]
})
export class SharedFiltersEditorModule { }
