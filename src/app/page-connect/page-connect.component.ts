import {Component, HostBinding, OnDestroy, ViewChild} from '@angular/core';
import {ConnectionState, SharedWebSocketService} from '@shared/services/shared-websocket.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {MdcLinearProgress} from '@angular-mdc/web';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/index';

@Component({
  templateUrl: './page-connect.component.html',
  styleUrls: ['./page-connect.component.scss']
})
export class PageConnectComponent implements OnDestroy {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';

  @ViewChild(MdcLinearProgress)
  private progressBar: MdcLinearProgress;
  private wsStateSub: Subscription;
  private wsStateErrorSub: Subscription;
  private form: FormGroup;

  // View bindings
  public bindings: any = {
    ctrl: this,

    get form(): FormGroup {
      return this.ctrl.form;
    }
  };

  public constructor(private wsService: SharedWebSocketService,
                     private snackbarService: SharedSnackbarService,
                     private formBuilder: FormBuilder,
                     private router: Router) {
    this.initForm();
    this.wsStateSub = this.wsService.onConnectivityChanged.subscribe((value: ConnectionState) => {
      if (value === ConnectionState.CONNECTED) {
        this.router.navigateByUrl('/clusters');
      }

      if (!this.isFormEnabled()) {
        this.form.disable();
      } else {
        this.form.enable();
      }
    });

    this.wsStateErrorSub = this.wsService.onError.subscribe((event: Event) => {
      this.snackbarService.show(`Connection lost with ${this.wsService.getAddress()}: ${(event as CloseEvent).reason}`);
    });
  }

  public ngOnDestroy(): void {
    this.wsStateErrorSub.unsubscribe();
    this.wsStateSub.unsubscribe();
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      protocol: ['http', Validators.required],
      host: ['localhost', Validators.required],
      port: ['7000', Validators.required],
      path: ['/monitor/ws']
    });

    if (!this.isFormEnabled()) {
      this.form.disable();
    }
  }

  public isFormEnabled(): boolean {
    return this.wsService.getState() === ConnectionState.DISCONNECTED;
  }

  public isConnectButtonVisible(): boolean {
    return this.wsService.getState() !== ConnectionState.CONNECTED;
  }

  public isConnectButtonEnabled(): boolean {
    return this.form.valid && this.wsService.getState() === ConnectionState.DISCONNECTED;
  }

  public isProgressVisible(): boolean {
    return this.wsService.getState() === ConnectionState.CONNECTING;
  }

  public isDisconnectButtonVisible(): boolean {
    return this.wsService.getState() === ConnectionState.CONNECTED;
  }

  public connect(): void {
    const address: string = `${this.form.value.protocol}://${this.form.value.host}:${this.form.value.port}${PageConnectComponent.prefixWithSlash(this.form.value.path)}`;
    this.wsService.connect(address);
  }

  public disconnect(): void {
    this.wsService.disconnect();
  }

  public static prefixWithSlash(url: string): string {
    return url.startsWith('/') ? url : `/${url}`;
  }
}
