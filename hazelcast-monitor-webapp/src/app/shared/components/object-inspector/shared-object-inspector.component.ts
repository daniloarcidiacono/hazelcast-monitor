import {Component, Input} from '@angular/core';
import {ObjectTreeNode} from "../mdc-tree/object-preorder-iterator";

@Component({
  selector: 'shared-object-inspector',
  templateUrl: './shared-object-inspector.component.html',
  styleUrls: [ './shared-object-inspector.component.scss' ]
})
export class SharedObjectInspectorComponent {
  @Input()
  private data: any;

  @Input()
  private stringData: string;

  @Input()
  private locked: boolean;

  public constructor() {
  }

  public depthClass(node: ObjectTreeNode): object {
    return {
      [`MdcTreeNode__Content--depth-${node.depth}`]: true
    };
  }

  public nodeCaption(node: ObjectTreeNode): string {
    if (node.parent === undefined) {
      return this.stringData;
    } else if (Array.isArray(node.data)) {
      return `${node.parentPropertyName}`;
    } else {
      return `${node.parentPropertyName}: ${JSON.stringify(node.data)}`;
    }
  }
}
