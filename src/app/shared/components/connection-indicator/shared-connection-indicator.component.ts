import {Component} from '@angular/core';
import {ConnectionState, SharedWebSocketService} from "@shared/services/shared-websocket.service";
import {SharedClustersService} from "@shared/services/shared-clusters.service";
import {MdcMenuItem} from "@angular-mdc/web";
import {Router} from "@angular/router";

enum PulseState {
  NONE,
  PULSE,
  REPULSE
}

@Component({
  selector: 'shared-connection-indicator',
  templateUrl: './shared-connection-indicator.component.html',
  styleUrls: [ './shared-connection-indicator.component.scss' ]
})
export class SharedConnectionIndicatorComponent {
  // View bindings
  public bindings: any = {
    ctrl: this
  };

  private pulse: PulseState = PulseState.NONE;

  public constructor(private wsService: SharedWebSocketService,
                     private clusterService: SharedClustersService,
                     private router: Router) {
    this.wsService.onMessageReceived.subscribe(
      (message: string) => {
        this.startPulse();
      }
    );
  }

  public handleMenuSelect(event: { index: number, item: MdcMenuItem }): void {
    if (event.index === 0) {
      this.router.navigateByUrl('/clusters');
    }

    if (event.index === 1) {
      this.wsService.disconnect();
    }
  }

  private startPulse(): void {
    if (this.pulse === PulseState.NONE) {
      this.pulse = PulseState.PULSE;
    } else if (this.pulse === PulseState.PULSE) {
      this.pulse = PulseState.REPULSE;
    } else {
      this.pulse = PulseState.PULSE;
    }
  }

  public get dotClass(): object {
    return {
      'ConnectionIndicator__Dot--connected': this.wsService.getState() === ConnectionState.CONNECTED,
      'ConnectionIndicator__Dot--pulse': this.wsService.getState() === ConnectionState.CONNECTED && this.pulse === PulseState.PULSE,
      'ConnectionIndicator__Dot--repulse': this.wsService.getState() === ConnectionState.CONNECTED && this.pulse === PulseState.REPULSE
    };
  }

  public get connectionUrl(): string {
    switch (this.wsService.getState()) {
      case ConnectionState.CONNECTED: {
        const address: string = this.wsService.getAddress();
        if (this.clusterService.hasCurrentCluster()) {
          const clusterName: string = this.clusterService.getCurrentCluster().instanceName;
          return `${clusterName}@${address}`;
        }

        return address;
      }

      default:
        return 'Not connected';
    }
  }
}
