import {CanActivate, Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {SharedClustersService} from "../services/shared-clusters.service";
import {ConnectionState, SharedWebSocketService} from "@shared/services/shared-websocket.service";

@Injectable()
export class SharedClusterGuard implements CanActivate {
  public constructor(private wsService: SharedWebSocketService,
                     private router: Router) {
  }

  public canActivate(): boolean {
    if (this.wsService.getState() === ConnectionState.CONNECTED) {
      return true;
    }

    this.router.navigateByUrl('/connect');
    return false;
  }
}
