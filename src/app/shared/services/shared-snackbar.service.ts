import {Injectable} from '@angular/core';
import {SharedWebStorageService} from "./shared-webstorage.service";
import {Cluster} from "../model/shared-cluster.model";
import {MdcSnackbar} from "@angular-mdc/web";

@Injectable()
export class SharedSnackbarService {
  public constructor(private snackbar: MdcSnackbar) {
  }

  public show(message: string, options?: any) {
    this.snackbar.show(message, options);
  }
}
