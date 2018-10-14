import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {
  ErrorMessageDTO,
  UpdateSubscriptionRequestDTO,
  UpdateSubscriptionResponseDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {AceEditorComponent} from "ng2-ace-editor";
import {MdcDialog} from "@angular-mdc/web";
import {SharedFiltersBarHelpDialog} from "@shared/components/filters-bar/shared-filters-bar-help.dialog";

@Component({
  selector: 'shared-filters-bar',
  templateUrl: './shared-filters-bar.component.html',
  styleUrls: [ './shared-filters-bar.component.scss' ]
})
export class SharedFiltersBarComponent implements AfterViewInit {
  @Input()
  private subscriptionId: number;

  @ViewChild(AceEditorComponent)
  private editor: AceEditorComponent;

  /////////////
  // Bindings
  /////////////
  public filter: string = '';

  /*
      "animatedScroll": false,
      "showInvisibles": false,
      "showPrintMargin": true,
      "printMarginColumn": 80,
      "printMargin": undefined,
      "showGutter": true,
      "fadeFoldWidgets": false,
      "showFoldWidgets": true,
      "showLineNumbers": true,
      "displayIndentGuides": true,
      "highlightGutterLine": false,
      "hScrollBarAlwaysVisible": false,
      "vScrollBarAlwaysVisible": false,
      "fontSize": 12,
      "fontFamily": undefined,
      "maxLines": undefined,
      "minLines": undefined,
      "maxPixelHeight": 0,
      "scrollPastEnd": 0,
      "fixedWidthGutter": undefined,
      "theme": "./theme/textmate"
   */
  public options: any = {
    maxLines: 1000,
    fontSize: 22,
    theme: 'ace/theme/chrome',
    maxPixelHeight: 400,
    printMargin: false
  };

  public constructor(private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private dialog: MdcDialog) {
  }

  public ngAfterViewInit(): void {
    this.editor.getEditor().commands.addCommand({
      name: 'apply',
      bindKey: 'Ctrl-Enter',
      exec: (editor) => {
        this.apply();
      }
    });
  }

  public showHelp(): void {
    this.dialog.open(SharedFiltersBarHelpDialog, {
      data: {
      },
      escapeToClose: true,
      clickOutsideToClose: true
    });
  }

  public apply(): void {
    const request: UpdateSubscriptionRequestDTO = {
      messageType: 'update_subscription',
      subscriptionId: this.subscriptionId,
      parameter: 'filter',
      value: this.filter
    };

    this.hazelcastService.sendUpdateSubscription(request).then((response: UpdateSubscriptionResponseDTO) => {
      if (!response.error) {
        this.snackbarService.show(`Error while update the filter ${response.error}`);
      } else {
        this.snackbarService.show('Filter updated');
      }
    }).catch((error: ErrorMessageDTO) => {
      this.snackbarService.show(`Compile error: ${error.errors.join('\n')}`);
    });
  }

  public isApplyButtonEnabled(): boolean {
    return this.subscriptionId !== undefined;
  }
}
