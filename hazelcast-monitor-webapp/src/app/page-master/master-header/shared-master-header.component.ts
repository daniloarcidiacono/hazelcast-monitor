import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: 'shared-master-header',
  templateUrl: './shared-master-header.component.html',
  styleUrls: [ '../shared-master.component.scss']
})
export class SharedMasterHeaderComponent {
  @Input()
  public caption: string;

  @Input()
  public filterRegex: string = '';

  @Output()
  public filterRegexChange: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  public search: EventEmitter<string> = new EventEmitter<string>();

  public constructor() {
  }

  public updateFilterRegex(value: string): void {
    this.filterRegex = value;
    this.filterRegexChange.emit(this.filterRegex);
  }

  public notifySearch(value: string): void {
    this.search.emit(value);
  }
}
