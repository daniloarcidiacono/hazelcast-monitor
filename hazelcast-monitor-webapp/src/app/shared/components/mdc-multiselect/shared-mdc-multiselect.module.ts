import {SharedMdcMultiSelectComponent} from '@shared/components/mdc-multiselect/shared-mdc-multiselect.component';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {
  MdcButtonModule, MdcCheckboxModule, MdcIconButtonModule, MdcListModule, MdcMenuModule, MdcSelectModule,
  MdcTypographyModule
} from '@angular-mdc/web';

@NgModule({
  declarations: [
    SharedMdcMultiSelectComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcButtonModule,
    MdcIconButtonModule,
    MdcTypographyModule,
    MdcListModule,
    MdcMenuModule,
    MdcCheckboxModule
  ],
  exports: [
    SharedMdcMultiSelectComponent
  ],
  providers: [
  ]
})
export class SharedMdcMultiSelectModule { }
