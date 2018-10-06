import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {PageTestComponent} from './page-test.component';
import {SharedMdcTreeModule} from '@shared/components/mdc-tree/shared-mdc-tree.module';
import {MdcButtonModule, MdcDialogModule, MdcIconModule} from '@angular-mdc/web';
import {SharedMdcTableModule} from '@shared/components/mdc-table/shared-mdc-table.module';
import {SharedMdcPaginatorModule} from '@shared/components/mdc-paginator/shared-mdc-paginator.module';
import {SharedObjectInspectorModule} from '@shared/components/object-inspector/shared-object-inspector.module';
import {MonacoEditorModule} from "ngx-monaco-editor";
import {FormsModule} from "@angular/forms";
import {PageTestDialogComponent} from "./page-test-dialog.component";

@NgModule({
  declarations: [
    PageTestComponent,
    PageTestDialogComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular Web MDC
    MdcIconModule,
    MdcButtonModule,
    MdcDialogModule,
    MonacoEditorModule,

    // Shared modules
    SharedMdcTreeModule,
    SharedObjectInspectorModule,
    SharedMdcTableModule,
    SharedMdcPaginatorModule
    // SharedMdcTreeTableModule
  ],
  providers: [
  ],
  entryComponents: [
    PageTestDialogComponent
  ]
})
export class PageTestModule { }
