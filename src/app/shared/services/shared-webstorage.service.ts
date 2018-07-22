import {Inject, Injectable} from '@angular/core';
import {SESSION_STORAGE, StorageService} from "ngx-webstorage-service";

@Injectable()
export class SharedWebStorageService {
  public constructor(@Inject(SESSION_STORAGE) private storage: StorageService) {
  }

  public get(key: string): string {
    return this.storage.get(key);
  }

  public set(key: string, value: string): void {
    this.storage.set(key, value);
  }

  public remove(key: string): void {
    this.storage.remove(key);
  }

  public has(key: string): boolean {
    return this.storage.has(key);
  }
}
