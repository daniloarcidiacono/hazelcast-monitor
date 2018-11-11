import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcTypographyModule} from '@angular-mdc/web';
import {SharedMdcTreeModule} from '@shared/components/mdc-tree/shared-mdc-tree.module';
import {SharedObjectInspectorComponent} from '@shared/components/object-inspector/shared-object-inspector.component';

@NgModule({
  declarations: [
    SharedObjectInspectorComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcButtonModule,
    MdcIconButtonModule,
    MdcIconModule,

    // Shared
    SharedMdcTableModule,
    SharedMdcTreeModule
  ],
  exports: [
    SharedObjectInspectorComponent
  ]
})
export class SharedObjectInspectorModule {
}
