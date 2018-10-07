import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';

@Component({
  templateUrl: './page-dashboard-filters.component.html',
  styleUrls: [ './page-dashboard-filters.component.scss' ]
})
export class PageDashboardFiltersComponent implements TabAwareComponent, OnDestroy {
  public constructor(private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public beforeShow(): void {
  }

  public beforeHide(): void {
  }

  public tabCreated(tab: TabData): void {
  }
}
