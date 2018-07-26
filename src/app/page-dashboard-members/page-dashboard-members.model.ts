import {MembersProductDTO} from '@shared/dto/topic-products.dto';
import {SharedMdcTableModel} from '@shared/components/mdc-table/shared-mdc-table.model';

export class MembersTableModel implements SharedMdcTableModel {
  private headerNames: string[] = [
    'IP Address',
    'Port',
    'Version',
    'UUID',
  ];

  private propertyMap: string[] = [
    'address',
    'port',
    'version',
    'uuid'
  ];

  public constructor(public members?: MembersProductDTO) {
  }

  public headerRowCount(): number {
    return 1;
  }

  public headerData(row: number, col: number): any {
    return this.headerNames[col];
  }

  public rowCount(): number {
    return !!this.members ? this.members.members.length : 0;
  }

  public colCount(): number {
    return this.propertyMap.length;
  }

  public data(row: number, col: number) {
    return this.members.members[row][this.propertyMap[col]];
  }
}
