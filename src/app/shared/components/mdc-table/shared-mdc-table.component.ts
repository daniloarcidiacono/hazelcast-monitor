import {
  Component, ContentChild, EventEmitter, Input, OnInit, Output, TemplateRef,
  ViewEncapsulation
} from '@angular/core';
import {SharedMdcTableModel} from "@shared/components/mdc-table/shared-mdc-table.model";
import {OutputDef} from "@angular/core/src/view";

@Component({
  selector: 'shared-mdc-table',
  templateUrl: './shared-mdc-table.component.html',
  styleUrls: [ './shared-mdc-table.component.scss' ],
  encapsulation: ViewEncapsulation.None
})
export class SharedMdcTableComponent implements OnInit {
  @Input()
  private model: SharedMdcTableModel;

  @Input()
  public selectable: boolean = false;

  @ContentChild('headerTemplate')
  private headerTemplate: TemplateRef<any>;

  @ContentChild('cellTemplate')
  private cellTemplate: TemplateRef<any>;

  @Output()
  private onRowClick: EventEmitter<number> = new EventEmitter<number>();

  public constructor() {
  }

  public ngOnInit(): void {
  }

  public signalBodyRowClick(row: number): void {
    this.onRowClick.emit(row);
  }
}
