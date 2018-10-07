import {Component, OnDestroy, ViewChild} from '@angular/core';
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
import {PageDashboardMapsComponent} from '../page-dashboard-maps/page-dashboard-maps.component';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {PageDashboardLocksComponent} from '../page-dashboard-locks/page-dashboard-locks.component';
import {PageDashboardListsComponent} from '../page-dashboard-lists/page-dashboard-lists.component';
import {PageDashboardMultiMapsComponent} from '../page-dashboard-multimaps/page-dashboard-multimaps.component';
import {PageDashboardReplicatedMapsComponent} from '../page-dashboard-replicatedmaps/page-dashboard-replicatedmaps.component';
import {PageDashboardSetsComponent} from '../page-dashboard-sets/page-dashboard-sets.component';
import {PageDashboardQueuesComponent} from '../page-dashboard-queues/page-dashboard-queues.component';
import {PageDashboardTopicsComponent} from '../page-dashboard-topics/page-dashboard-topics.component';
import {PageDashboardAtomicLongsComponent} from '../page-dashboard-atomiclongs/page-dashboard-atomiclongs.component';
import {PageDashboardAtomicReferencesComponent} from '../page-dashboard-atomicreferences/page-dashboard-atomicreferences.component';
import {PageDashboardCountdownLatchesComponent} from '../page-dashboard-countdownlatches/page-dashboard-countdownlatches.component';
import {PageDashboardSemaphoresComponent} from '../page-dashboard-semaphores/page-dashboard-semaphores.component';
import {PageDashboardCachesComponent} from '../page-dashboard-caches/page-dashboard-caches.component';
import {PageDashboardRingbuffersComponent} from '../page-dashboard-ringbuffers/page-dashboard-ringbuffers.component';
import {PageDashboardInternalsComponent} from '../page-dashboard-internals/page-dashboard-internals.component';
import {PageDashboardFiltersComponent} from '../page-dashboard-filters/page-dashboard-filters.component';
import {FontIcon, SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';

interface SeparatorItem {
  separator: boolean;
}

interface SectionItem {
  icon: FontIcon;
  caption: string;
  count: () => number;
  tab: TabData;
  subHeader?: string;
}

type DashboardSection = SeparatorItem | SectionItem;

@Component({
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.scss']
})
export class PageDashboardComponent implements OnDestroy {
  @ViewChild(SharedDynamicTabsComponent)
  private tabsComponent: SharedDynamicTabsComponent;

  public sections: DashboardSection[] = [
    {
      icon: SharedPageIconsConstants.MEMBERS_ICON,
      caption: 'Members',
      count: () => {
        return !!this.currentStats ? this.currentStats.membersCount : undefined;
      },
      tab: {
        label: 'Members',
        componentClass: PageDashboardMembersComponent
      },
      subHeader: 'General'
    },
    {
      icon: SharedPageIconsConstants.FILTERS_ICON,
      caption: 'Filters',
      count: () => {
        return undefined;
      },
      tab: {
        label: 'Filters',
        componentClass: PageDashboardFiltersComponent
      }
    },
    {
      separator: true
    },
    {
      icon: SharedPageIconsConstants.ATOMICLONGS_ICON,
      caption: 'Atomic longs',
      count: () => {
        return !!this.currentStats ? this.currentStats.atomicLongCount : undefined;
      },
      tab: {
        label: 'AtomicLongs',
        componentClass: PageDashboardAtomicLongsComponent
      },
      subHeader: 'Resources'
    },
    {
      icon: SharedPageIconsConstants.ATOMICREFERENCES_ICON,
      caption: 'Atomic references',
      count: () => {
        return !!this.currentStats ? this.currentStats.atomicReferenceCount : undefined;
      },
      tab: {
        label: 'AtomicReferences',
        componentClass: PageDashboardAtomicReferencesComponent
      }
    },
    {
      icon: SharedPageIconsConstants.CACHES_ICON,
      caption: 'Caches',
      count: () => {
        return !!this.currentStats ? this.currentStats.cacheCount : undefined;
      },
      tab: {
        label: 'Caches',
        componentClass: PageDashboardCachesComponent
      }
    },
    {
      icon: SharedPageIconsConstants.COUNTDOWNLATCHES_ICON,
      caption: 'Count-down latches',
      count: () => {
        return !!this.currentStats ? this.currentStats.countDownLatchCount : undefined;
      },
      tab: {
        label: 'CountDownLatches',
        componentClass: PageDashboardCountdownLatchesComponent
      }
    },
    {
      icon: SharedPageIconsConstants.LISTS_ICON,
      caption: 'Lists',
      count: () => {
        return !!this.currentStats ? this.currentStats.listCount : undefined;
      },
      tab: {
        label: 'Lists',
        componentClass: PageDashboardListsComponent
      }
    },
    {
      icon: SharedPageIconsConstants.LOCKS_ICON,
      caption: 'Locks',
      count: () => {
        return !!this.currentStats ? this.currentStats.lockCount : undefined;
      },
      tab: {
        label: 'Locks',
        componentClass: PageDashboardLocksComponent
      }
    },
    {
      icon: SharedPageIconsConstants.MAPS_ICON,
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
      icon: SharedPageIconsConstants.MULTIMAPS_ICON,
      caption: 'Multi-maps',
      count: () => {
        return !!this.currentStats ? this.currentStats.multiMapCount : undefined;
      },
      tab: {
        label: 'MultiMaps',
        componentClass: PageDashboardMultiMapsComponent
      }
    },
    {
      icon: SharedPageIconsConstants.QUEUES_ICON,
      caption: 'Queues',
      count: () => {
        return !!this.currentStats ? this.currentStats.queueCount : undefined;
      },
      tab: {
        label: 'Queues',
        componentClass: PageDashboardQueuesComponent
      }
    },
    {
      icon: SharedPageIconsConstants.REPLICATEDMAPS_ICON,
      caption: 'Replicated maps',
      count: () => {
        return !!this.currentStats ? this.currentStats.replicatedMapCount : undefined;
      },
      tab: {
        label: 'ReplicatedMaps',
        componentClass: PageDashboardReplicatedMapsComponent
      }
    },
    {
      icon: SharedPageIconsConstants.RINGBUFFERS_ICON,
      caption: 'Ringbuffers',
      count: () => {
        return !!this.currentStats ? this.currentStats.ringbufferCount : undefined;
      },
      tab: {
        label: 'Ringbuffers',
        componentClass: PageDashboardRingbuffersComponent
      }
    },
    {
      icon: SharedPageIconsConstants.SEMAPHORES_ICON,
      caption: 'Semaphores',
      count: () => {
        return !!this.currentStats ? this.currentStats.semaphoreCount : undefined;
      },
      tab: {
        label: 'Semaphores',
        componentClass: PageDashboardSemaphoresComponent
      }
    },
    {
      icon: SharedPageIconsConstants.SETS_ICON,
      caption: 'Sets',
      count: () => {
        return !!this.currentStats ? this.currentStats.setCount : undefined;
      },
      tab: {
        label: 'Sets',
        componentClass: PageDashboardSetsComponent
      }
    },
    {
      icon: SharedPageIconsConstants.TOPICS_ICON,
      caption: 'Topics',
      count: () => {
        return !!this.currentStats ? this.currentStats.topicCount : undefined;
      },
      tab: {
        label: 'Topics',
        componentClass: PageDashboardTopicsComponent
      }
    },
    {
      separator: true
    },
    {
      icon: SharedPageIconsConstants.MONITORINTERNALS_ICON,
      caption: 'Monitor internals',
      count: () => {
        return undefined;
      },
      tab: {
        label: 'Monitor internals',
        componentClass: PageDashboardInternalsComponent
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

    // Use the drawer icons in the tabs
    for (const section of this.sections) {
      if (!!(section as SectionItem).tab) {
        (section as SectionItem).tab.icon = (section as SectionItem).icon;
      }
    }

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
      // Note: we need to clone the tab descriptor, otherwise opening the same section multiple times
      // won't work. Also notice that object spread is a shallow copy, but for our purposes it is sufficient.
      this.tabService.addTab(
        {
          ...section.tab
        }
      );
    }
  }
}
