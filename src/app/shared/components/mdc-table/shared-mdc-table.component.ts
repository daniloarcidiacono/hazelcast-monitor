import {
  Component, ContentChild, EventEmitter, Input, Output, TemplateRef, ViewEncapsulation,
} from '@angular/core';

@Component({
  selector: 'shared-mdc-table',
  templateUrl: './shared-mdc-table.component.html',
  styleUrls: [ './shared-mdc-table.component.scss' ],
  encapsulation: ViewEncapsulation.None
})
export class SharedMdcTableComponent {
  @Input()
  private model: any[];

  @Input()
  public selectable: boolean = false;

  @ContentChild('headerTemplate')
  private headerTemplate: TemplateRef<any>;

  @ContentChild('bodyTemplate')
  private bodyTemplate: TemplateRef<any>;

  @Output()
  private onRowClick: EventEmitter<number> = new EventEmitter<number>();

  public constructor() {
  }
}
