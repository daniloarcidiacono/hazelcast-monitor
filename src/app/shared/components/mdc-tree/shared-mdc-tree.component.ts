import {Component, ContentChild, Input, OnChanges, SimpleChanges, TemplateRef, ViewEncapsulation} from '@angular/core';
import {ObjectPreorderIterator, ObjectTreeNode} from "./object-preorder-iterator";

@Component({
  selector: 'shared-mdc-tree',
  templateUrl: './shared-mdc-tree.component.html',
  styleUrls: [ './shared-mdc-tree.component.scss' ],
  encapsulation: ViewEncapsulation.None
})
export class SharedMdcTreeComponent implements OnChanges {
  @Input()
  private model: any;

  @ContentChild(TemplateRef)
  private template: TemplateRef<any>;

  private tree: ObjectPreorderIterator;

  private internalModel: any = {
  };

  public constructor() {
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if ('model' in changes) {
      this.tree = new ObjectPreorderIterator(this.model, this.injectModel.bind(this));
    }
  }

  private injectModel(path: string): any {
    if (!(path in this.internalModel)) {
      this.internalModel[path] = {
        collapsed: true
      };
    }

    return this.internalModel[path];
  }

  public toggleCollapse(node: ObjectTreeNode) {
    if (!(node.path in this.internalModel)) {
      this.internalModel[node.path] = {
        collapsed: true
      };
    }

    this.internalModel[node.path].collapsed = !this.internalModel[node.path].collapsed;
  }

  public trackFunc(index: number, data: ObjectTreeNode): any {
    return data.path;
  }
}
