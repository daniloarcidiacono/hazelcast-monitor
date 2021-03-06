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
import {PageDashboardMapsComponent} from '../page-master/master-maps/page-dashboard-maps.component';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {PageDashboardLocksComponent} from '../page-master/master-locks/page-dashboard-locks.component';
import {PageDashboardListsComponent} from '../page-master/master-lists/page-dashboard-lists.component';
import {PageDashboardMultiMapsComponent} from '../page-master/master-multimaps/page-dashboard-multimaps.component';
import {PageDashboardReplicatedMapsComponent} from '../page-master/master-replicatedmaps/page-dashboard-replicatedmaps.component';
import {PageDashboardSetsComponent} from '../page-master/master-sets/page-dashboard-sets.component';
import {PageDashboardQueuesComponent} from '../page-master/master-queues/page-dashboard-queues.component';
import {PageDashboardTopicsComponent} from '../page-master/master-topics/page-dashboard-topics.component';
import {PageDashboardAtomicLongsComponent} from '../page-master/master-atomiclongs/page-dashboard-atomiclongs.component';
import {PageDashboardSemaphoresComponent} from '../page-master/master-semaphores/page-dashboard-semaphores.component';
import {PageDashboardRingbuffersComponent} from '../page-master/master-ringbuffers/page-dashboard-ringbuffers.component';
import {PageDashboardInternalsComponent} from '../page-dashboard-internals/page-dashboard-internals.component';
import {FontIcon, SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardExecutorsComponent} from "../page-master/master-executors/page-dashboard-executors.component";
import {PageDashboardAtomicReferencesComponent} from "../page-master/master-atomicreferences/page-dashboard-atomicreferences.component";
import {PageDashboardCachesComponent} from "../page-master/master-caches/page-dashboard-caches.component";
import {PageDashboardCountdownLatchesComponent} from "../page-master/master-countdownlatches/page-dashboard-countdownlatches.component";
import {PageDashboardCardinalityEstimatorsComponent} from "../page-master/master-cardinalityestimators/page-dashboard-cardinalityestimators.component";

interface SeparatorItem {
  separator: boolean;
}

interface SectionItem {
  icon: FontIcon;
  caption: string;
  count: () => number;
  tab: TabData;
  subHeader?: string;
  color: string;
}

type DashboardSection = SeparatorItem | SectionItem;

@Component({
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.scss']
})
export class PageDashboardComponent implements OnDestroy {
  @ViewChild(SharedDynamicTabsComponent)
  private tabsComponent: SharedDynamicTabsComponent;

  // Tile colors
  private colors: string[] = [
    'blueviolet',
    'brown',
    'cadetblue',
    'chocolate',
    'coral',
    'cornflowerblue',
    'crimson',
    'midnightblue',
    'darkgoldenrod',
    'darkgreen',
    'darkmagenta',
    'teal',
    'sienna',
    'seagreen',
    'darkorange',
    'lightseagreen',
    'blueviolet',
    'brown'
  ];

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
      subHeader: 'General',
      color: 'red'
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
      },
      color: 'red'
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
      subHeader: 'Resources',
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
    },
    {
      icon: SharedPageIconsConstants.CARDINALITY_ICON,
      caption: 'Cardinality estimators',
      count: () => {
        return !!this.currentStats ? this.currentStats.cardinalityEstimatorsCount : undefined;
      },
      tab: {
        label: 'Cardinality estimators',
        componentClass: PageDashboardCardinalityEstimatorsComponent
      },
      color: 'red'
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
      },
      color: 'red'
    },
    {
      icon: SharedPageIconsConstants.EXECUTOR_ICON,
      caption: 'Executors',
      count: () => {
        return !!this.currentStats ? this.currentStats.executorsCount : undefined;
      },
      tab: {
        label: 'Executors',
        componentClass: PageDashboardExecutorsComponent
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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
      },
      color: 'red'
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


    let colorIdx = 0;
    for (const section of this.sections) {
      if (!!(section as SectionItem).tab) {
        // Use the drawer icons in the tabs
        (section as SectionItem).tab.icon = (section as SectionItem).icon;

        // Init the colors
        (section as SectionItem).color = this.colors[colorIdx];
        colorIdx = (colorIdx + 1) % this.colors.length;
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
