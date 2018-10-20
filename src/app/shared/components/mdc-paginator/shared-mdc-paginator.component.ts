import {Component, EventEmitter, Input, Output} from '@angular/core';
import {isNumber} from "util";

@Component({
  selector: 'shared-mdc-paginator',
  templateUrl: './shared-mdc-paginator.component.html',
  styleUrls: [ './shared-mdc-paginator.component.scss' ]
})
export class SharedMdcPaginatorComponent {
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

  public trackPageChange(value: string): void {
    const parsed: number = parseInt(value, 10);

    if (!isNaN(parsed)) {
      this.page = parsed;
      this.pageChange.emit(this.page);
    }
  }

  public trackPageSizeChange(value: string): void {
    const parsed: number = parseInt(value, 10);
    if (!isNaN(parsed)) {
      this.pageSize = parsed;
      this.pageSizeChange.emit(this.pageSize);
    }
  }

  public nextPage(): void {
    this.page++;
    this.pageChange.emit(this.page);
  }

  public previousPage(): void {
    if (this.page > 1) {
      this.page--;
      this.pageChange.emit(this.page);
    }
  }
}
