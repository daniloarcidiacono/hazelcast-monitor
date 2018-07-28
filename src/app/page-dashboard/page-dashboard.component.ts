import {Component, ContentChild, HostBinding, OnDestroy, ViewChild} from '@angular/core';
import {ConnectionState, SharedWebSocketService} from '@shared/services/shared-websocket.service';
import {Router} from '@angular/router';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {StatisticsProductDTO} from '@shared/dto/topic-products.dto';
import {SharedDynamicTabsComponent} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.component';
import {PageDashboardMembersComponent} from '../page-dashboard-members/page-dashboard-members.component';
import {PageDashboardMapsComponent} from "../page-dashboard-maps/page-dashboard-maps.component";
import {SharedTabsService} from "@shared/services/shared-tabs.service";
import {TabData} from "@shared/components/dynamic-tabs/shared-dynamic-tabs.model";

@Component({
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.scss']
})
export class PageDashboardComponent implements OnDestroy {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';

  @ViewChild(SharedDynamicTabsComponent)
  private tabsComponent: SharedDynamicTabsComponent;

  private wsStateSub: Subscription;
  private statsSub: Subscription;
  private tabSub: Subscription;
  private currentStats: StatisticsProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private wsService: SharedWebSocketService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabService: SharedTabsService,
                     private router: Router) {
    this.wsStateSub = this.wsService.onConnectivityChanged.subscribe((value: ConnectionState) => {
      if (value !== ConnectionState.CONNECTED) {
        this.snackbarService.show(`Connection lost with ${this.wsService.getAddress()}`);
        this.router.navigateByUrl('/connect');
      }
    });

    this.statsSub = this.hazelcastService.subscribeToStatistics(this.clustersService.getCurrentCluster().instanceName).subscribe(
      (notice: SubscriptionNoticeResponseDTO<StatisticsProductDTO>) => {
        this.currentStats = notice.notice;
      },
      (error: ErrorMessageDTO) => {
        this.snackbarService.show(`Could not fetch the statistics: ${error.errors}`);
      }
    );

    this.tabSub = this.tabService.tabAdded.subscribe((tab: TabData) => {
      this.tabsComponent.addTab(tab);
    });
  }

  public ngOnDestroy(): void {
    this.tabSub.unsubscribe();
    this.statsSub.unsubscribe();
    this.wsStateSub.unsubscribe();
  }

  public navigateTo(section: string): void {
    if (section === 'members') {
      this.tabService.addTab(
        {
          label: 'Members',
          componentClass: PageDashboardMembersComponent
        }
      );
    }

    if (section === 'maps') {
      this.tabService.addTab(
        {
          label: 'Maps',
          componentClass: PageDashboardMapsComponent
        }
      );
    }

    /*
    this.router.navigate([
      '/dashboard',
      {
        outlets: {
          'section': section
        }
      }
    ]);*/
  }
}
