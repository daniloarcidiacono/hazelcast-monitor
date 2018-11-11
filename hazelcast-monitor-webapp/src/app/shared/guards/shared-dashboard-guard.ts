import {CanActivate, Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {SharedClustersService} from "../services/shared-clusters.service";

@Injectable()
export class SharedDashboardGuard implements CanActivate {
  public constructor(private clustersService: SharedClustersService,
                     private router: Router) {
  }

  public canActivate(): boolean {
    if (this.clustersService.hasCurrentCluster()) {
      return true;
    }

    this.router.navigateByUrl('/clusters');
    return false;
  }
}
