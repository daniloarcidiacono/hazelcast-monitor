import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {SharedMdcTreeComponent} from '@shared/components/mdc-tree/shared-mdc-tree.component';

@NgModule({
  declarations: [
    SharedMdcTreeComponent
  ],
  imports: [
    BrowserModule
  ],
  exports: [
    SharedMdcTreeComponent
  ],
  providers: [
  ]
})
export class SharedMdcTreeModule { }
