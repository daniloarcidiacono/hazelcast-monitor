import {AfterContentInit, AfterViewInit, Component} from '@angular/core';
import {MdcDialogRef} from '@angular-mdc/web';
import {NgxMonacoEditorConfig} from "ngx-monaco-editor";
import ICodeEditor = monaco.editor.ICodeEditor;

@Component({
  template: `
  <mdc-dialog>
    <mdc-dialog-surface>
      <mdc-dialog-header>
        <mdc-dialog-header-title>
          Filter
        </mdc-dialog-header-title>
      </mdc-dialog-header>
      <mdc-dialog-body>
        <ngx-monaco-editor class="my-code-editor" [options]="editorOptions" [(ngModel)]="code" (onInit)="onEditorInitialized($event)"></ngx-monaco-editor>
      </mdc-dialog-body>
      <mdc-dialog-footer>
        <button mdc-dialog-button [accept]="true">Ok</button>
      </mdc-dialog-footer>
    </mdc-dialog-surface>
  </mdc-dialog>
  `,
  styles: [
    `
      .my-code-editor {
        height: calc(100vh - 200px);
      }
    `
  ]
})
export class PageTestDialogComponent {
  public editorOptions: any = {
    theme: 'vs',
    language: 'java',
    minimap: {
      enabled: false
    }
  };

  public code: string = `
    public class App {
      public static void main(String[] args) {
        System.out.println("Hello, world!");
      }
    }
  `;

  public constructor(public dialogRef: MdcDialogRef<PageTestDialogComponent>) {
  }

  public onEditorInitialized(editor: ICodeEditor): void {
    this.patchFocusTrap(editor.getDomNode());
  }

  /**
   * Prevents the TAB key to be handled by the focus trap created automatically by mdc-dialog.
   *
   *    // Focus trap in dialog.es5.js
   *    // handleTab is invoked in the capturing phase!
   *    function handleTab(e) {
   *         updateTabbableNodes();
   *
   *         if (e.target.hasAttribute('tabindex') && Number(e.target.getAttribute('tabindex')) < 0) {
   *           return tabEvent = e;
   *         }
   *
   *         e.preventDefault();
   *         ...
   *     }
   *
   * @param {HTMLElement} editorDomNode
   */
  private patchFocusTrap(editorDomNode: HTMLElement): void {
    const inputarea: HTMLTextAreaElement = editorDomNode.querySelector('textarea.inputarea');
    if (!!inputarea) {
      inputarea.setAttribute('tabindex', '-1');
      console.info('ngx-monaco-editor focustrap patch applied');
    } else {
      console.warn('ngx-monaco-editor focustrap patch failed: input area not found~');
    }
  }
}
