import {IHelpButtonDialogData, SharedHelpButtonDialog} from "@shared/components/help-button/shared-help-button.dialog";
import {Component, ContentChild, Input, TemplateRef} from "@angular/core";
import {MdcDialog} from "@angular-mdc/web";

@Component({
  selector: 'shared-help-button',
  templateUrl: './shared-help-button.component.html',
  styleUrls: [ './shared-help-button.component.scss' ]
})
export class SharedHelpButtonComponent {
  @Input()
  private title: string;

  @ContentChild(TemplateRef)
  private template: TemplateRef<any>;

  public constructor(private dialog: MdcDialog) {
  }

  public showHelp(): void {
    this.dialog.open(SharedHelpButtonDialog, {
      data: {
        title: this.title,
        template: this.template
      },
      escapeToClose: true,
      clickOutsideToClose: true
    });
  }
}
