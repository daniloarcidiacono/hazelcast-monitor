import {
  MdcButtonModule,
  MdcIconButtonModule,
  MdcIconModule,
  MdcSelectModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {ReactiveFormsModule} from '@angular/forms';
import {SharedFiltersBarComponent} from '@shared/components/filters-bar/shared-filters-bar.component';
import {SharedFiltersEditorModule} from '@shared/components/filters-editor/shared-filters-editor.module';

@NgModule({
  declarations: [
    SharedFiltersBarComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconButtonModule,
    MdcIconModule,
    MdcButtonModule,
    MdcIconButtonModule,
    MdcSelectModule,

    // Shared
    SharedFiltersEditorModule
  ],
  exports: [
    SharedFiltersBarComponent
  ],
  providers: [
    SharedServicesModule
  ]
})
export class SharedFiltersBarModule { }
