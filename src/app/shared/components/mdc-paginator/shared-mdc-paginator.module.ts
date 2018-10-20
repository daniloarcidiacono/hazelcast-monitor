import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedMdcPaginatorComponent} from '@shared/components/mdc-paginator/shared-mdc-paginator.component';
import {
  MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcSelectModule, MdcTextFieldModule,
  MdcTypographyModule
} from '@angular-mdc/web';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    SharedMdcPaginatorComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcButtonModule,
    MdcIconButtonModule,
    MdcSelectModule,
    MdcIconModule,
    MdcTypographyModule,
    MdcTextFieldModule,

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
