import {Injectable} from '@angular/core';
import {MdcSnackbar} from "@angular-mdc/web";

@Injectable()
export class SharedSnackbarService {
  public constructor(private snackbar: MdcSnackbar) {
  }

  public show(message: string) {
    this.snackbar.open(message);
  }
}
