import {MapsProductDTO} from '@shared/dto/topic-products.dto';
import {SharedMdcTableModel} from '@shared/components/mdc-table/shared-mdc-table.model';

export class MapsTableModel implements SharedMdcTableModel {
  private headerNames: string[] = [
    'Name',
    'Number of keys'
  ];

  private propertyMap: string[] = [
    'name',
    'size'
  ];

  public constructor(public maps?: MapsProductDTO) {
  }

  public headerRowCount(): number {
    return 1;
  }

  public headerData(row: number, col: number): any {
    return this.headerNames[col];
  }

  public rowCount(): number {
    return !!this.maps ? this.maps.maps.length : 0;
  }

  public colCount(): number {
    return this.propertyMap.length;
  }

  public data(row: number, col: number) {
    return this.maps.maps[row][this.propertyMap[col]];
  }
}
