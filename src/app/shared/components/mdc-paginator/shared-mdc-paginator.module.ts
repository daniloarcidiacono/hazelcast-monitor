import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedMdcPaginatorComponent} from '@shared/components/mdc-paginator/shared-mdc-paginator.component';
import {MdcButtonModule, MdcIconModule, MdcSelectModule} from '@angular-mdc/web';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';

@NgModule({
  declarations: [
    SharedMdcPaginatorComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcButtonModule,
    MdcSelectModule,
    MdcIconModule,

    // Shared
    SharedPipesModule
  ],
  exports: [
    SharedMdcPaginatorComponent
  ],
  providers: [
  ]
})
export class SharedMdcPaginatorModule { }
