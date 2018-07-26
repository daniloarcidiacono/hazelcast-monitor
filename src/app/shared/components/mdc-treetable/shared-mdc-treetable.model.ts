import {SharedMdcTreeNodeModel} from "@shared/components/mdc-tree/shared-mdc-tree.model";

export interface SharedMdcTreeTableModel {
  tree(): SharedMdcTreeNodeModel;
  // Column count (tree excluded)
  colCount(): number;
  headerRowCount(): number;
  headerData(row: number, col: number): any;
  data(row: SharedMdcTreeNodeModel, col: number): any;
}
