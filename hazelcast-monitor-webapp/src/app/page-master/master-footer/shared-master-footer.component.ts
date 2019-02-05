import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: 'shared-master-footer',
  templateUrl: './shared-master-footer.component.html',
  styleUrls: [ '../shared-master.component.scss']
})
export class SharedMasterFooterComponent {
  @Input()
  public queryTime: number;

  @Input()
  public page: number = 1;

  @Input()
  public pageSize: number = 5;

  @Output()
  public pageChange: EventEmitter<number> = new EventEmitter<number>();

  @Output()
  public pageSizeChange: EventEmitter<number> = new EventEmitter<number>();

  public constructor() {
  }

  public updatePage(value: number): void {
    this.page = value;
    this.pageChange.emit(this.page);
  }

  public updatePageSize(value: number): void {
    this.pageSize = value;
    this.pageSizeChange.emit(this.pageSize);
  }
}
