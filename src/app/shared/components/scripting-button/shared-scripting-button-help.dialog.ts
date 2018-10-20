import {Component, Inject} from '@angular/core';
import {MDC_DIALOG_DATA, MdcDialogRef} from '@angular-mdc/web';

export interface IScriptingButtonHelpDialogData {
  title: string;
  htmlContent: string;
}

@Component({
  template: `
  <mdc-dialog>
    <mdc-dialog-container>
      <mdc-dialog-surface>
        <mdc-dialog-title>
          {{ data.title }}
        </mdc-dialog-title>
        <mdc-dialog-content [innerHTML]="data.htmlContent | safe:'html'">
        </mdc-dialog-content>
        <mdc-dialog-actions>
          <button mdcDialogButton mdcDialogAction="accept">Ok</button>
        </mdc-dialog-actions>
      </mdc-dialog-surface>
    </mdc-dialog-container>
  </mdc-dialog>
  `,
})
export class SharedScriptingButtonHelpDialog {
  public constructor(public dialogRef: MdcDialogRef<SharedScriptingButtonHelpDialog>,
                     @Inject(MDC_DIALOG_DATA) public data: IScriptingButtonHelpDialogData) {
  }
}
