import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {ObjectMdcTreeNodeModel, SharedMdcTreeNodeModel} from "@shared/components/mdc-tree/shared-mdc-tree.model";

@Component({
  selector: 'shared-object-inspector',
  templateUrl: './shared-object-inspector.component.html',
  styleUrls: [ './shared-object-inspector.component.scss' ]
})
export class SharedObjectInspectorComponent implements OnChanges {
  @Input()
  private data: any;

  @Input()
  private stringData: string;

  @Input()
  private locked: boolean;

  public treeModel: ObjectMdcTreeNodeModel;

  public constructor() {
  }

  public ngOnChanges(changes: SimpleChanges) {
    if ('data' in changes) {
      if (this.isComplex()) {
        this.treeModel = new ObjectMdcTreeNodeModel(this.data, undefined, undefined);
      } else {
        this.treeModel = undefined;
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

  public isComplex(): boolean {
    return typeof this.data === 'object';
  }
}
