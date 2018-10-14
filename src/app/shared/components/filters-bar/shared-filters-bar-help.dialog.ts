import {Component, Inject} from '@angular/core';
import {MDC_DIALOG_DATA, MdcDialogRef} from '@angular-mdc/web';

@Component({
  template: `
  <mdc-dialog>
    <mdc-dialog-container>
      <mdc-dialog-surface>
        <mdc-dialog-title>
          Filter scripting
        </mdc-dialog-title>
        <mdc-dialog-content>
          <ul>
            <li>JavaScript ES6 syntax is supported.</li>
            <li>Press CTRL+Enter to apply the current filter.</li>
            <li>Use <pre style="display: inline">element</pre> to refer to the current element.</li>
            <li><strong>Don't use</strong> the <pre style="display: inline">return</pre> keyword, just write the condition.</li>
          </ul>
        </mdc-dialog-content>
        <mdc-dialog-actions>
          <button mdcDialogButton mdcDialogAction="accept">Ok</button>
        </mdc-dialog-actions>
      </mdc-dialog-surface>
    </mdc-dialog-container>
  </mdc-dialog>
  `,
})
export class SharedFiltersBarHelpDialog {
  public constructor(public dialogRef: MdcDialogRef<SharedFiltersBarHelpDialog>,
                     @Inject(MDC_DIALOG_DATA) public data: any) {
  }
}
