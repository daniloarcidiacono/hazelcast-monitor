import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {ObjectMdcTreeNodeModel, SharedMdcTreeNodeModel} from "@shared/components/mdc-tree/shared-mdc-tree.model";

@Component({
  selector: 'map-tree',
  templateUrl: './page-dashboard-map-tree.component.html',
  styleUrls: [ './page-dashboard-map-tree.component.scss' ]
})
export class PageDashboardMapTreeComponent implements OnChanges {
  @Input()
  private data: any;

  public model: ObjectMdcTreeNodeModel;

  public constructor() {
  }

  public ngOnChanges(changes: SimpleChanges) {
    if ('data' in changes) {
      if (changes['data']) {
        this.model = new ObjectMdcTreeNodeModel(this.data, undefined, undefined);
      }
    }
  }

  public calcDepth(node: SharedMdcTreeNodeModel): number {
    let depth = 1;
    let curNode: SharedMdcTreeNodeModel = node;
    while (curNode.parent() !== undefined) {
      depth++;
      curNode = curNode.parent();
    }

    return depth;
  }

  public depthClass(node: SharedMdcTreeNodeModel): object {
    return {
      [`MdcTreeNode__Content--depth-${this.calcDepth(node)}`]: true
    };
  }

  public isArray(node: SharedMdcTreeNodeModel) {
    return Array.isArray(typeof (node as ObjectMdcTreeNodeModel).data);
  }
}
