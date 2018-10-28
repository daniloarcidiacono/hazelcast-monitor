import {Component, Inject, TemplateRef} from '@angular/core';
import {MDC_DIALOG_DATA, MdcDialogRef} from '@angular-mdc/web';

export interface IHelpButtonDialogData {
  title: string;
  template: TemplateRef<any>;
}

@Component({
  template: `
  <mdc-dialog>
    <mdc-dialog-container>
      <mdc-dialog-surface>
        <mdc-dialog-title>
          <div style="display: inline-flex; align-items: center;">
            <mdc-icon style="margin-right: 12px;">help_outline</mdc-icon>
            <span>
              Help: "{{ data.title }}"
            </span>
          </div>
        </mdc-dialog-title>
        <mdc-dialog-content>
          <ng-container *ngTemplateOutlet="data.template"></ng-container>
        </mdc-dialog-content>
        <mdc-dialog-actions>
          <button mdcDialogButton mdcDialogAction="accept">Ok</button>
        </mdc-dialog-actions>
      </mdc-dialog-surface>
    </mdc-dialog-container>
  </mdc-dialog>
  `,
})
export class SharedHelpButtonDialog {
  public constructor(public dialogRef: MdcDialogRef<SharedHelpButtonDialog>,
                     @Inject(MDC_DIALOG_DATA) public data: IHelpButtonDialogData) {
  }
}
