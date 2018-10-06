import {Component} from '@angular/core';
import {MdcDialogRef} from '@angular-mdc/web';

@Component({
  template: `
  <mdc-dialog>
    <mdc-dialog-surface>
      <mdc-dialog-header>
        <mdc-dialog-header-title>
          Notice
        </mdc-dialog-header-title>
      </mdc-dialog-header>
      <mdc-dialog-body>
        <p>Can't inspect {{ dialogRef.data.ringbufferName}}: ringbuffer inspection is not supported.</p>
      </mdc-dialog-body>
      <mdc-dialog-footer>
        <button mdc-dialog-button [accept]="true">Ok</button>
      </mdc-dialog-footer>
    </mdc-dialog-surface>
  </mdc-dialog>
  `,
})
export class PageDashboardRingbuffersDialogComponent {
  public constructor(public dialogRef: MdcDialogRef<PageDashboardRingbuffersDialogComponent>) {
  }
}
