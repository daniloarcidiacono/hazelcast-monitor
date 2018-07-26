export interface SharedMdcTreeNodeModel {
  parent(): SharedMdcTreeNodeModel;
  children(): SharedMdcTreeNodeModel[];
  collapsed(): boolean;
  collapse(collapsed: boolean): void;
}

export class ObjectMdcTreeNodeModel implements SharedMdcTreeNodeModel {
  // Children are constructed only on demand
  private _children: ObjectMdcTreeNodeModel[];
  private _collapsed: boolean = true;

  public constructor(public data: object, public parentPropertyName: string, private _parent: ObjectMdcTreeNodeModel) {
  }

  public parent(): SharedMdcTreeNodeModel {
    return this._parent;
  }

  public children(): SharedMdcTreeNodeModel[] {
    if (this._children === undefined) {
      // For default, non-objects do not have any child
      this._children = [];

      // If we have an object (note: typeof null === 'object')
      if (typeof this.data === 'object' && this.data !== null) {
        // Push the sub properties
        for (const propName of Object.keys(this.data)) {
          this._children.push(new ObjectMdcTreeNodeModel(this.data[propName], propName, this));
        }
      }
    }

    // Return the children
    return this._children;
  }

  public collapsed(): boolean {
    return this._collapsed;
  }

  public collapse(collapsed: boolean): void {
    this._collapsed = collapsed;
  }
}

// Note: IterableIterator is not supported by Angular
// https://github.com/angular/angular/issues/14275
export class TreePreorderIterator implements Iterable<SharedMdcTreeNodeModel> {
  public constructor(private root: SharedMdcTreeNodeModel) {
  }

  [Symbol.iterator]() {
    const stack: SharedMdcTreeNodeModel[] = [ this.root ];
    const result: any = {
      next(): IteratorResult<SharedMdcTreeNodeModel> {
        if (stack.length === 0) {
          return {
            done: true,
            value: null
          };
        }

        const current: SharedMdcTreeNodeModel = stack.shift();
        if (!current.collapsed()) {
          const children: SharedMdcTreeNodeModel[] = current.children();
          for (let i = children.length - 1; i >= 0; i--) {
            stack.unshift(children[i]);
          }
        }

        return {
          done: false,
          value: current
        };
      }
    };

    this.root.collapse(false);
    result.next();
    return result;
  }
}
