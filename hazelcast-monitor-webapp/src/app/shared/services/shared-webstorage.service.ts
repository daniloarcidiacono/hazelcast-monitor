import {Inject, Injectable} from '@angular/core';
import {LOCAL_STORAGE, StorageService} from "ngx-webstorage-service";

@Injectable()
export class SharedWebStorageService {
  public constructor(@Inject(LOCAL_STORAGE) private storage: StorageService) {
  }

  public get(key: string): string {
    return this.storage.get(key);
  }

  public getAsObject(key: string): any {
    try {
      return JSON.parse(this.storage.get(key))
    } catch (e) {
      return undefined;
    }
  }

  public set(key: string, value: string): void {
    this.storage.set(key, value);
  }

  public setAsObject(key: string, value: any): void {
    this.set(key, JSON.stringify(value));
  }

  public remove(key: string): void {
    this.storage.remove(key);
  }

  public has(key: string): boolean {
    return this.storage.has(key);
  }
}
