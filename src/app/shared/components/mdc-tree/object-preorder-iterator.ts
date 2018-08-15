export class ObjectTreeNode {
  // Parent node
  parent: ObjectTreeNode;

  // Data of the node
  data: any;

  // Name of the property
  parentPropertyName: string;

  // Full path to the property
  path: string;

  // Number of children
  childCount: number;

  // Depth in the tree
  depth: number;

  // User data calculated by an external function
  other: any;
}

// Note: IterableIterator is not supported by Angular
// https://github.com/angular/angular/issues/14275
export class ObjectPreorderIterator implements Iterable<ObjectTreeNode> {
  public constructor(private root: any, private decoratorFn: any) {
  }

  public calcChildCount(data: any): number {
    if (typeof data === 'object' && data !== null) {
      return Object.keys(data).length;
    }

    return 0;
  }

  public [Symbol.iterator]() {
    const stack: ObjectTreeNode[] = [
      {
        parent: undefined,
        data: this.root,
        parentPropertyName: undefined,
        path: '',
        depth: 1,
        childCount: this.calcChildCount(this.root),
        other: this.decoratorFn('')
      }
    ];

    const self = this;
    const result: any = {
      next(): IteratorResult<ObjectTreeNode> {
        if (stack.length === 0) {
          return {
            done: true,
            value: null
          };
        }

        const current: ObjectTreeNode = stack.shift();
        const collapsed: boolean = current.other !== undefined &&
                                   'collapsed' in current.other &&
                                   current.other['collapsed'] === true;

        // If we have an object (note: typeof null === 'object')
        if (typeof current.data === 'object' && current.data !== null && !collapsed) {
          const children: ObjectTreeNode[] = [];

          for (const childProperty of Object.keys(current.data)) {
            let newPath: string;
            if (current.data === self.root) {
              newPath = `${childProperty}`;
            } else {
              newPath = Array.isArray(current.data) ? `${current.path}[${childProperty}]`: `${current.path}.${childProperty}`;
            }

            children.push(
              {
                parent: current,
                data: current.data[childProperty],
                parentPropertyName: childProperty,
                path: newPath,
                depth: current.depth + 1,
                childCount: self.calcChildCount(current.data[childProperty]),
                other: self.decoratorFn(newPath)
              }
            );
          }

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

    // result.next();
    return result;
  }
}
