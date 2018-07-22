import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {PageClustersComponent} from './page-clusters/page-clusters.component';
import {PageDashboardComponent} from './page-dashboard/page-dashboard.component';
import {PageNotFoundComponent} from './page-notfound/page-notfound.component';
import {SharedClusterGuard} from '@shared/guards/shared-cluster.guard';
import {SharedServicesModule} from '@shared/services/shared-services.module';
import {PageConnectComponent} from './page-connect/page-connect.component';
import {SharedDashboardGuard} from '@shared/guards/shared-dashboard-guard';

@NgModule({
  imports: [
    RouterModule.forRoot([
      {
        path: 'connect',
        component: PageConnectComponent
      },
      {
        path: 'dashboard',
        component: PageDashboardComponent,
        canActivate: [
          SharedDashboardGuard
        ]
      },
      {
        path: 'clusters',
        component: PageClustersComponent,
        canActivate: [
          SharedClusterGuard
        ]
      },
      {
        path: '',
        redirectTo: '/clusters',
        pathMatch: 'full'
      },
      { path: '**', component: PageNotFoundComponent }
    ]),
    SharedServicesModule
  ],
  providers: [
  ]
})
export class AppRoutingModule {
}
