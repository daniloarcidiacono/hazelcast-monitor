import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'shared-mdc-paginator',
  templateUrl: './shared-mdc-paginator.component.html',
  styleUrls: [ './shared-mdc-paginator.component.scss' ]
})
export class SharedMdcPaginatorComponent {
  @Input()
  public total: number;

  public current: number = 0;

  public constructor() {
  }

/*
  @Output()
  public currentPageChange = new EventEmitter<number>();

  @Input()
  public get currentPage(): number {
    return this.currentPageValue;
  }

  public set currentPage(val: number) {
    this.currentPageValue = val;
    this.currentPageChange.emit(this.currentPageValue);
  }
  */
}
