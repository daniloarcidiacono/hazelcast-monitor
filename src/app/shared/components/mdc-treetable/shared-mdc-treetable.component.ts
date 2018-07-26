import {Component, ContentChild, Input, OnInit, TemplateRef} from '@angular/core';
import {SharedMdcTreeNodeModel, TreePreorderIterator} from '@shared/components/mdc-tree/shared-mdc-tree.model';
import {SharedMdcTreeTableModel} from '@shared/components/mdc-treetable/shared-mdc-treetable.model';

@Component({
  selector: 'shared-mdc-treetable',
  templateUrl: './shared-mdc-treetable.component.html',
  styleUrls: [ './shared-mdc-treetable.component.scss' ]
})
export class SharedMdcTreeTableComponent implements OnInit {
  @Input()
  private model: SharedMdcTreeTableModel;

  @ContentChild(TemplateRef)
  private template: TemplateRef<any>;

  private tree: TreePreorderIterator;

  public constructor() {
  }

  public ngOnInit(): void {
    this.tree = new TreePreorderIterator(this.model.tree());
  }

  public toggleCollapse(node: SharedMdcTreeNodeModel) {
    node.collapse(!node.collapsed());
  }
}
