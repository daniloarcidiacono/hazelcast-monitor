import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'shared-search-bar',
  templateUrl: './shared-search-bar.component.html',
  styleUrls: [ './shared-search-bar.component.scss' ]
})
export class SharedSearchBarComponent {
  @Input()
  public filterRegex: string = '';

  @Output()
  public filterRegexChange: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  public search: EventEmitter<string> = new EventEmitter<string>();

  public constructor() {
  }

  public handleKeydown(event: KeyboardEvent): void {
    if (event.ctrlKey && event.which === 13) {
      this.search.emit(this.filterRegex);
    }
  }

  public updateFilterRegex(value: string): void {
    this.filterRegex = value;
    this.filterRegexChange.emit(this.filterRegex);
  }
}
