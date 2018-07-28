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

interface SeparatorItem {
  separator: boolean;
}
interface SectionItem {
  icon: string;
  caption: string;
  count: () => number;
  tab: TabData;
}

type DashboardSection = SeparatorItem | SectionItem;

@Component({
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.scss']
})
export class PageDashboardComponent implements OnDestroy {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';

  @ViewChild(SharedDynamicTabsComponent)
  private tabsComponent: SharedDynamicTabsComponent;

  public sections: DashboardSection[] = [
    {
      icon: 'group',
      caption: 'Members',
      count: () => {
        return !!this.currentStats ? this.currentStats.membersCount : undefined;
      },
      tab: {
        label: 'Members',
        componentClass: PageDashboardMembersComponent
      }
    },
    {
      separator: true
    },
    {
      icon: '',
      caption: 'Atomic longs',
      count: () => {
        // @TODO: AtomicLongs statistics
        return undefined;
      },
      tab: {
        label: 'AtomicLongs',
        componentClass: undefined
      }
    },
    {
      icon: '',
      caption: 'Atomic references',
      count: () => {
        // @TODO: AtomicReferences statistics
        return undefined;
      },
      tab: {
        label: 'AtomicReferences',
        componentClass: undefined
      }
    },
    {
      icon: 'memory',
      caption: 'Caches',
      count: () => {
        // @TODO: Caches statistics
        return undefined;
      },
      tab: {
        label: 'Caches',
        componentClass: undefined
      }
    },
    {
      icon: '',
      caption: 'Count-down latches',
      count: () => {
        // @TODO: CountDonwLatches statistic
        return undefined;
      },
      tab: {
        label: 'CountDownLatches',
        componentClass: undefined
      }
    },
    {
      icon: 'format_list_numbered',
      caption: 'Lists',
      count: () => {
        return !!this.currentStats ? this.currentStats.listCount : undefined;
      },
      tab: {
        label: 'Lists',
        componentClass: undefined
      }
    },
    {
      icon: 'lock',
      caption: 'Locks',
      count: () => {
        return !!this.currentStats ? this.currentStats.lockCount : undefined;
      },
      tab: {
        label: 'Locks',
        componentClass: undefined
      }
    },
    {
      icon: 'map',
      caption: 'Maps',
      count: () => {
        return !!this.currentStats ? this.currentStats.mapCount : undefined;
      },
      tab: {
        label: 'Maps',
        componentClass: PageDashboardMapsComponent
      }
    },
    {
      icon: 'map',
      caption: 'Multi-maps',
      count: () => {
        return !!this.currentStats ? this.currentStats.multiMapCount : undefined;
      },
      tab: {
        label: 'MultiMaps',
        componentClass: undefined
      }
    },
    {
      icon: 'list',
      caption: 'Queues',
      count: () => {
        return !!this.currentStats ? this.currentStats.queueCount : undefined;
      },
      tab: {
        label: 'Queues',
        componentClass: undefined
      }
    },
    {
      icon: 'map',
      caption: 'Replicated maps',
      count: () => {
        // @TODO: Replicated maps stats
        return undefined;
      },
      tab: {
        label: 'ReplicatedMaps',
        componentClass: undefined
      }
    },
    {
      icon: '360',
      caption: 'Ringbuffers',
      count: () => {
        // @TODO: Ringbuffer stats
        return undefined;
      },
      tab: {
        label: 'Ringbuffers',
        componentClass: undefined
      }
    },
    {
      icon: 'traffic',
      caption: 'Semaphores',
      count: () => {
        // @TODO: Sempahores stats
        return undefined;
      },
      tab: {
        label: 'Semaphores',
        componentClass: undefined
      }
    },
    {
      icon: '',
      caption: 'Sets',
      count: () => {
        return !!this.currentStats ? this.currentStats.setCount : undefined;
      },
      tab: {
        label: 'Sets',
        componentClass: undefined
      }
    },
    {
      icon: 'question_answer',
      caption: 'Topics',
      count: () => {
        return !!this.currentStats ? this.currentStats.topicCount : undefined;
      },
      tab: {
        label: 'Topics',
        componentClass: undefined
      }
    },
    {
      separator: true
    },
    {
      icon: 'bug_report',
      caption: 'Monitor internals',
      count: () => {
        return undefined;
      },
      tab: {
        label: 'Monitor internals',
        componentClass: undefined
      }
    }
  ];

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

  public navigateTo(section: SectionItem): void {
    if (section.tab.componentClass !== undefined) {
      this.tabService.addTab(section.tab);
    }
  }
}
