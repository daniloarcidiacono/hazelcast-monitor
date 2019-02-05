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
  public model: any[];

  @Input()
  public selectable: boolean = false;

  @ContentChild('headerTemplate')
  public headerTemplate: TemplateRef<any>;

  @ContentChild('bodyTemplate')
  public bodyTemplate: TemplateRef<any>;

  @ContentChild('footerTemplate')
  public footerTemplate: TemplateRef<any>;

  @Output()
  private onRowClick: EventEmitter<number> = new EventEmitter<number>();

  public constructor() {
  }
}
