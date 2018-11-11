import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {MdcCheckboxChange} from "@angular-mdc/web";

export interface MdcMultiSelectOption {
  name: string;
  value: any;
}

@Component({
  selector: 'shared-mdc-multiselect',
  templateUrl: './shared-mdc-multiselect.component.html',
  styleUrls: [ './shared-mdc-multiselect.component.scss' ]
})
export class SharedMdcMultiSelectComponent implements OnChanges {
  @Input()
  public placeholder: string;

  @Input()
  public disabled: boolean = false;

  @Input()
  public options: MdcMultiSelectOption[];

  @Output()
  public selectionChange: EventEmitter<any[]> = new EventEmitter<any[]>();

  // Set that keeps track of the selected values
  private checkedValues: Set<any> = new Set<any>();

  public constructor() {
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('options')) {
      // When options changes, we remove the selection of non-existent values
      const existingValues: Set<any> = new Set(this.options.map(option => option.value));
      this.checkedValues = new Set([...this.checkedValues].filter(x => existingValues.has(x)));
    }
  }

  public trackCheck(option: MdcMultiSelectOption, event: MdcCheckboxChange) {
    if (event.checked) {
      this.checkedValues.add(option.value);
    } else {
      this.checkedValues.delete(option.value);
    }

    this.selectionChange.emit(Array.from(this.checkedValues));
  }

  public isChecked(option: MdcMultiSelectOption): boolean {
    return this.checkedValues.has(option.value);
  }

  public getSelectionLabel(): string {
    const selectedOptionDisplayNames: string[] = this.options.filter(option => this.checkedValues.has(option.value)).map(option => option.name);
    if (selectedOptionDisplayNames.length === 1) {
      return selectedOptionDisplayNames[0];
    }

    return selectedOptionDisplayNames.join(', ');
  }

  public hasSelection(): boolean {
    return this.checkedValues.size > 0;
  }
}
