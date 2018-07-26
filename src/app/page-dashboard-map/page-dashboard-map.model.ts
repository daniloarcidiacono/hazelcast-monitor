import {MapProductDTO} from '@shared/dto/topic-products.dto';
import {SharedMdcTableModel} from '@shared/components/mdc-table/shared-mdc-table.model';

export interface MapTableModelNode {
  value: any;
  stringValue: string;
  complex: boolean;
}

export class MapTableModel implements SharedMdcTableModel {
  private headerNames: string[] = [
    'Key',
    'Value'
  ];

  public constructor(public map?: MapProductDTO) {
  }

  public headerRowCount(): number {
    return 1;
  }

  public headerData(row: number, col: number): any {
    return this.headerNames[col];
  }

  public rowCount(): number {
    return !!this.map ? this.map.data.length : 0;
  }

  public colCount(): number {
    return 2;
  }

  public data(row: number, col: number): MapTableModelNode {
    if (col === 0) {
      return {
        value: this.map.data[row].key,
        stringValue: this.map.data[row].keyString,
        complex: (typeof this.map.data[row].key === 'object')
      };
    }

    return {
      value: this.map.data[row].value,
      stringValue: this.map.data[row].valueString,
      complex: (typeof this.map.data[row].value === 'object')
    };
  }
}
