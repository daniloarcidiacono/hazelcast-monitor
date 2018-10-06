import {Component, Inject} from '@angular/core';
import {MDC_DIALOG_DATA, MdcDialogRef} from '@angular-mdc/web';

@Component({
  template: `
  <mdc-dialog>
    <mdc-dialog-container>
      <mdc-dialog-surface>
        <mdc-dialog-title>
          Notice
        </mdc-dialog-title>
        <mdc-dialog-content>
          <p>Can't inspect {{ data.ringbufferName }}: ringbuffer inspection is not supported.</p>
        </mdc-dialog-content>
        <mdc-dialog-actions>
          <button mdcDialogButton mdcDialogAction="accept">Ok</button>
        </mdc-dialog-actions>
      </mdc-dialog-surface>
    </mdc-dialog-container>
  </mdc-dialog>
  `,
})
export class PageDashboardRingbuffersDialogComponent {
  public constructor(public dialogRef: MdcDialogRef<PageDashboardRingbuffersDialogComponent>,
                     @Inject(MDC_DIALOG_DATA) public data: any) {
  }
}
