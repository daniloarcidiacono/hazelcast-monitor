import {EventEmitter, Injectable} from "@angular/core";
import {TabData} from "@shared/components/dynamic-tabs/shared-dynamic-tabs.model";

@Injectable()
export class SharedTabsService {
  public tabAdded: EventEmitter<TabData> = new EventEmitter<TabData>();

  public constructor() {
  }

  public addTab(data: TabData): void {
    this.tabAdded.emit(data);
  }
}
