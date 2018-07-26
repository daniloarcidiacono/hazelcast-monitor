import {Component, ContentChild, Input, OnInit, TemplateRef, ViewChild, ViewEncapsulation} from '@angular/core';
import {
  ObjectMdcTreeNodeModel, SharedMdcTreeNodeModel,
  TreePreorderIterator
} from '@shared/components/mdc-tree/shared-mdc-tree.model';

@Component({
  selector: 'shared-mdc-tree',
  templateUrl: './shared-mdc-tree.component.html',
  styleUrls: [ './shared-mdc-tree.component.scss' ],
  encapsulation: ViewEncapsulation.None
})
export class SharedMdcTreeComponent implements OnInit {
  @Input()
  private model: SharedMdcTreeNodeModel;

  @ContentChild(TemplateRef)
  private template: TemplateRef<any>;

  private tree: TreePreorderIterator;

  public constructor() {
  }

  public ngOnInit(): void {
    this.tree = new TreePreorderIterator(this.model);
  }

  public toggleCollapse(node: SharedMdcTreeNodeModel) {
    node.collapse(!node.collapsed());
  }
}
