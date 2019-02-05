import {AfterViewInit, Component, EventEmitter, Input, Output, QueryList, ViewChildren} from '@angular/core';
import {AceEditorComponent} from 'ng2-ace-editor';

@Component({
  selector: 'shared-scripting-button',
  templateUrl: './shared-scripting-button.component.html',
  styleUrls: [ './shared-scripting-button.component.scss' ]
})
export class SharedScriptingButtonComponent implements AfterViewInit {
  @Input()
  public filterScript: string = 'true';

  @Input()
  public sliceScript: string = '$..*';

  @Output()
  private filterScriptChange: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  private sliceScriptChange: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  public apply: EventEmitter<void> = new EventEmitter<void>();

  @ViewChildren(AceEditorComponent)
  private editors: QueryList<AceEditorComponent>;

  public options: any = {
    maxLines: 1000,
    fontSize: 22,
    maxPixelHeight: 400,
    printMargin: false
  };

  public constructor() {
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

  public updateFilterScript(newScript: string): void {
    this.filterScript = newScript;
    this.filterScriptChange.emit(this.filterScript);
  }

  public updateSliceScript(newScript: string): void {
    this.sliceScript = newScript;
    this.sliceScriptChange.emit(this.sliceScript);
  }
}
