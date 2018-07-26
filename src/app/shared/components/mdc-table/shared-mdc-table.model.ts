export interface SharedMdcTableModel {
  rowCount(): number;
  colCount(): number;
  headerRowCount(): number;
  headerData(row: number, col: number): any;
  data(row: number, col: number): any;
}
