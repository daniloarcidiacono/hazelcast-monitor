import {AfterViewInit, Component, EventEmitter, Input, Output, QueryList, ViewChildren} from '@angular/core';
import {MdcDialog} from '@angular-mdc/web';
import {
  IScriptingButtonHelpDialogData,
  SharedScriptingButtonHelpDialog
} from '@shared/components/scripting-button/shared-scripting-button-help.dialog';
import {AceEditorComponent} from 'ng2-ace-editor';

@Component({
  selector: 'shared-scripting-button',
  templateUrl: './shared-scripting-button.component.html',
  styleUrls: [ './shared-scripting-button.component.scss' ]
})
export class SharedScriptingButtonComponent implements AfterViewInit {
  @Input()
  private filterScript: string = 'true';

  @Input()
  private sliceScript: string = '$..*';

  @Output()
  private filterScriptChange: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  private sliceScriptChange: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  public apply: EventEmitter<void> = new EventEmitter<void>();

  @ViewChildren(AceEditorComponent)
  private editors: QueryList<AceEditorComponent>;

  public filterHelpDialogData: IScriptingButtonHelpDialogData = {
    title: 'Filter scripting',
    htmlContent: `
      <ul>
        <li>ES6 syntax is supported.</li>
        <li>Press CTRL+Enter to apply the filter.</li>
        <li>Use <pre style="display: inline">element</pre> to refer to the current element.</li>
        <li><strong>Don't use</strong> the <pre style="display: inline">return</pre> keyword, just write the condition.</li>
        <li>Compare strings with <pre style="display: inline">==</pre> instead of <pre style="display: inline">===</pre>.</li>
        <li>If your script throws an error for an element, it is considered as returning <pre style="display: inline">false</pre>.</li>
        <li>If your script does not compile, an empty set is returned.</li>
      </ul>
    `
  };

  public slicerHelpDialogData: IScriptingButtonHelpDialogData = {
    title: 'Slice scripting',
    htmlContent: `
      <ul>
        <li>Language is JsonPath.</li>
        <li>Press CTRL+Enter to apply the slice.</li>
      </ul>
    `
  };

  public options: any = {
    maxLines: 1000,
    fontSize: 22,
    theme: 'ace/theme/chrome',
    maxPixelHeight: 400,
    printMargin: false
  };

  public constructor(private dialog: MdcDialog) {
  }

  public ngAfterViewInit(): void {
    this.editors.forEach((editor) => {
      editor.getEditor().commands.addCommand({
        name: 'apply',
        bindKey: 'Ctrl-Enter',
        exec: () => {
          this.apply.emit();
        }
      });
    });
  }

  public showHelp(dialogData: IScriptingButtonHelpDialogData): void {
    this.dialog.open(SharedScriptingButtonHelpDialog, {
      data: dialogData,
      escapeToClose: true,
      clickOutsideToClose: true
    });
  }

  public updateFilterScript(newScript: string): void {
    this.filterScript = newScript;
    this.filterScriptChange.emit(this.filterScript);
  }

  public updateSliceScript(newScript: string): void {
    this.sliceScript = newScript;
    this.sliceScriptChange.emit(this.sliceScript);
  }
}
