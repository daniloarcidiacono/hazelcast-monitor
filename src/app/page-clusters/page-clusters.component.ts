import {Component, HostBinding, OnDestroy} from '@angular/core';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Cluster} from '@shared/model/shared-cluster.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConnectionState, SharedWebSocketService} from '@shared/services/shared-websocket.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {ClustersProductDTO} from '@shared/dto/topic-products.dto';

@Component({
  templateUrl: './page-clusters.component.html',
  styleUrls: ['./page-clusters.component.scss']
})
export class PageClustersComponent implements OnDestroy {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';

  private form: FormGroup;
  private wsStateSub: Subscription;
  private clusterSub: Subscription;

  // View bindings
  public bindings: any = {
    ctrl: this,

    get form(): FormGroup {
      return this.ctrl.form;
    },

    get clusters(): Cluster[] {
      return this.ctrl.clustersService.getClusters();
    },

    get hasClusters(): boolean {
      return this.clusters.length > 0;
    }
  };

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private wsService: SharedWebSocketService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private router: Router,
                     private fb: FormBuilder) {
    this.initForm();

    this.wsStateSub = this.wsService.onConnectivityChanged.subscribe((value: ConnectionState) => {
      if (value !== ConnectionState.CONNECTED) {
        this.snackbarService.show(`Connection lost with ${this.wsService.getAddress()}`);
        this.router.navigateByUrl('/connect');
      }
    });

    this.clusterSub = this.hazelcastService.subscribeToClusters().subscribe(
      (notice: SubscriptionNoticeResponseDTO<ClustersProductDTO>) => {
        const newData: Cluster[] = notice.notice.clusters.map(name => new Cluster(name));
        this.clustersService.setClusters(newData);
        this.form.patchValue({
          cluster: this.clustersService.getClusters()[0]
        });
      },
      (error: ErrorMessageDTO) => {
        this.form.disable();
        this.snackbarService.show(`Could not fetch the instances: ${error.errors}`);
      }
    );
  }

  public ngOnDestroy(): void {
    this.clusterSub.unsubscribe();
    this.wsStateSub.unsubscribe();
  }

  private initForm(): void {
    this.form = this.fb.group({
      cluster: [ undefined, Validators.required ]
    });
  }

  public hasClusters(): boolean {
    return this.clustersService.hasClusters();
  }

  public isNextButtonEnabled(): boolean {
    return this.form.valid;
  }

  public next(): void {
    this.clustersService.setCurrentCluster(this.form.value.cluster);
    this.router.navigateByUrl('/dashboard');
  }
}
