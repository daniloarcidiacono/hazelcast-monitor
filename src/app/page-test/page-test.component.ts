// import {Component, HostBinding} from '@angular/core';
// import {
//   ObjectMdcTreeNodeModel, SharedMdcTreeNodeModel,
//   TreePreorderIterator
// } from "@shared/components/mdc-tree/shared-mdc-tree.model";
// import {SharedMdcTableModel} from "@shared/components/mdc-table/shared-mdc-table.model";
// import {SharedMdcTreeTableModel} from "@shared/components/mdc-treetable/shared-mdc-treetable.model";

import {Component, HostBinding} from '@angular/core';

@Component({
  templateUrl: './page-test.component.html',
  styleUrls: ['./page-test.component.scss']
})
export class PageTestComponent {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';

  private tableModel: any[];

  public constructor() {
    this.tableModel = [
      { brand: 'Apple', lastYearSale: '51%', thisYearSale: '40%', lastYearProfit: '$54,406.00', thisYearProfit: '$43,342' },
      { brand: 'Samsung', lastYearSale: '83%', thisYearSale: '96%', lastYearProfit: '$423,132', thisYearProfit: '$312,122' },
      { brand: 'Microsoft', lastYearSale: '38%', thisYearSale: '5%', lastYearProfit: '$12,321', thisYearProfit: '$8,500' },
      { brand: 'Philips', lastYearSale: '49%', thisYearSale: '22%', lastYearProfit: '$745,232', thisYearProfit: '$650,323,' },
      { brand: 'Song', lastYearSale: '17%', thisYearSale: '79%', lastYearProfit: '$643,242', thisYearProfit: '500,332' },
      { brand: 'LG', lastYearSale: '52%', thisYearSale: ' 65%', lastYearProfit: '$421,132', thisYearProfit: '$150,005' },
      { brand: 'Sharp', lastYearSale: '82%', thisYearSale: '12%', lastYearProfit: '$131,211', thisYearProfit: '$100,214' },
      { brand: 'Panasonic', lastYearSale: '44%', thisYearSale: '45%', lastYearProfit: '$66,442', thisYearProfit: '$53,322' },
      { brand: 'HTC', lastYearSale: '90%', thisYearSale: '56%', lastYearProfit: '$765,442', thisYearProfit: '$296,232' },
      { brand: 'Toshiba', lastYearSale: '75%', thisYearSale: '54%', lastYearProfit: '$21,212', thisYearProfit: '$12,533' }
    ];
  }

  // public data: any = {
  //   name: 'Danilo',
  //   surname: 'Arcidiacono',
  //   address: {
  //     name: 'Via Arzaga',
  //     number: 23,
  //     other: [
  //       {
  //         id: 1,
  //         name: 'Pineapple',
  //         tasty: true
  //       },
  //       {
  //         id: 2,
  //         name: 'Watermelon',
  //         tasty: false
  //       }
  //     ]
  //   },
  //   stats: [
  //     49,
  //     20,
  //     154
  //   ]
  // };
  //
  // public tableData: any[] = [
  //   {
  //     "name": "Frozen yogurt",
  //     "calories": 159,
  //     "fat": 6,
  //     "carbs": 24,
  //     "protein": 4
  //   },
  //   {
  //     "name": "Ice cream sandwich",
  //     "calories": 237,
  //     "fat": 9,
  //     "carbs": 37,
  //     "protein": 4.3
  //   },
  //   {
  //     "name": "Eclair",
  //     "calories": 262,
  //     "fat": 16,
  //     "carbs": 24,
  //     "protein": 6
  //   },
  //   {
  //     "name": "Cupcake",
  //     "calories": 305,
  //     "fat": 3.7,
  //     "carbs": 67,
  //     "protein": 3.9
  //   },
  //   {
  //     "name": "Gingerbread",
  //     "calories": 356,
  //     "fat": 16,
  //     "carbs": 49,
  //     "protein": 0
  //   },
  //   {
  //     "name": "Jelly bean",
  //     "calories": 375,
  //     "fat": 0,
  //     "carbs": 94,
  //     "protein": 0
  //   },
  //   {
  //     "name": "Lollipop",
  //     "calories": 392,
  //     "fat": 0.2,
  //     "carbs": 98,
  //     "protein": 6.5
  //   },
  //   {
  //     "name": "Honeycomb",
  //     "calories": 408,
  //     "fat": 3.2,
  //     "carbs": 87,
  //     "protein": 4.9
  //   }
  // ];
  //
  // public model: ObjectMdcTreeNodeModel;
  // public tableModel: SharedMdcTableModel;
  // public treeTableModel: SharedMdcTreeTableModel;
  // public headerNames: string[] = [
  //   'Dessert (100g serving)',
  //   'Calories',
  //   'Fat (g)',
  //   'Carbs (g)',
  //   'Protein (g)'
  // ];
  // public propertyMap: string[] = [
  //   'name',
  //   'calories',
  //   'fat',
  //   'carbs',
  //   'protein'
  // ];
  //
  // public constructor() {
  //   // for (let i = 0; i < 1000; i++) {
  //   //   this.data.stats.push(i);
  //   // }
  //
  //   this.model = new ObjectMdcTreeNodeModel(this.data, undefined, undefined);
  //   this.tableModel = {
  //     headerRowCount: (): number => {
  //       return 1;
  //     },
  //     headerData: (row: number, col: number): any => {
  //       return this.headerNames[col];
  //     },
  //     rowCount: (): number => {
  //       return this.tableData.length;
  //     },
  //     colCount: (): number => {
  //       return this.propertyMap.length;
  //     },
  //     data: (row: number, col: number) => {
  //       return this.tableData[row % this.tableData.length][this.propertyMap[col]];
  //     }
  //   };
  //
  //   this.treeTableModel = {
  //     tree: (): SharedMdcTreeNodeModel => {
  //       return this.model;
  //     },
  //     // Column count (tree excluded)
  //     colCount: (): number => {
  //       return 1;
  //     },
  //     headerRowCount: (): number => {
  //       return 1;
  //     },
  //     headerData: (row: number, col: number): any => {
  //       if (col === 0) {
  //         return 'Key';
  //       }
  //
  //       return 'Value';
  //     },
  //     data: (row: SharedMdcTreeNodeModel, col: number): any => {
  //       const result: any = (row as ObjectMdcTreeNodeModel).data;
  //       if (typeof result !== 'object') {
  //         return result;
  //       } else {
  //         return '';
  //       }
  //     }
  //   }
  //
  // }
  //
  // public addProperty(): void {
  //   this.data['ciao'] = true;
  // }
  //
  // public calcDepth(node: SharedMdcTreeNodeModel): number {
  //   let depth = 1;
  //   let curNode: SharedMdcTreeNodeModel = node;
  //   while (curNode.parent() !== undefined) {
  //     depth++;
  //     curNode = curNode.parent();
  //   }
  //
  //   return depth;
  // }
  //
  // public depthClass(node: SharedMdcTreeNodeModel): object {
  //   return {
  //     [`MdcTreeNode__Content--depth-${this.calcDepth(node)}`]: true
  //   };
  // }
  //
  // public isArray(node: SharedMdcTreeNodeModel) {
  //   return Array.isArray(typeof (node as ObjectMdcTreeNodeModel).data);
  // }
}
