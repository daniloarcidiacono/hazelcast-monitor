import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedMdcTableComponent} from '@shared/components/mdc-table/shared-mdc-table.component';
import {MdcCheckboxModule} from '@angular-mdc/web';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';

@NgModule({
  declarations: [
    SharedMdcTableComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcCheckboxModule,

    // Shared
    SharedPipesModule
  ],
  exports: [
    SharedMdcTableComponent
  ],
  providers: [
  ]
})
export class SharedMdcTableModule { }
