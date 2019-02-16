import {Component, OnDestroy} from '@angular/core';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Cluster} from '@shared/model/shared-cluster.model';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConnectionState, SharedWebSocketService} from '@shared/services/shared-websocket.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/index';
import {
  AuthenticateResponseDTO,
  ErrorMessageDTO,
  SubscriptionNoticeResponseDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {ClustersProductDTO} from '@shared/dto/topic-products.dto';

@Component({
  templateUrl: './page-clusters.component.html',
  styleUrls: ['./page-clusters.component.scss']
})
export class PageClustersComponent implements OnDestroy {
  private form: FormGroup;
  private clusterControl: FormControl;
  private wsStateSub: Subscription;
  private clusterSub: Subscription;
  private isLoading: boolean = false;

  // View bindings
  public bindings: any = {
    ctrl: this,

    get form(): FormGroup {
      return this.ctrl.form;
    },

    get currentCluster(): Cluster {
      return this.ctrl.form.value.cluster !== undefined ? this.ctrl.clustersService.getClusters()[parseInt(this.ctrl.form.value.cluster)] : undefined;
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
    this.clustersService.reset();
    this.initForm();

    this.wsStateSub = this.wsService.onConnectivityChanged.subscribe((value: ConnectionState) => {
      if (value !== ConnectionState.CONNECTED) {
        this.snackbarService.show(`Connection lost with ${this.wsService.getAddress()}`);
        this.router.navigateByUrl('/connect');
      }
    });

    this.form.markAsPending();
    this.clusterSub = this.hazelcastService.subscribeToClusters().subscribe(
      (notice: SubscriptionNoticeResponseDTO<ClustersProductDTO>) => {
        const newData: Cluster[] = notice.notice.clusters.map(cluster => new Cluster(cluster.instanceName, cluster.groupName));
        this.clustersService.setClusters(newData);
        if (!this.hasClusters()) {
          this.clusterControl.disable();
        } else {
          this.clusterControl.enable();
        }

        if (this.form.pristine) {
          this.form.patchValue({
            cluster: '0'
          });
        }
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

  public trackClusterFn(cluster: Cluster): any {
    return cluster ? cluster.instanceName : undefined;
  }


  private initForm(): void {
    this.clusterControl = this.fb.control({ undefined, disabled: false }, Validators.required);

    this.form = this.fb.group({
      cluster: this.clusterControl,
      password: [ undefined ]
    });
  }

  public hasClusters(): boolean {
    return this.clustersService.hasClusters();
  }

  public isProgressVisible(): boolean {
    return this.wsService.getState() === ConnectionState.CONNECTING;
  }

  public isNextButtonEnabled(): boolean {
    return this.form.valid && !this.isLoading;
  }

  public next(): void {
    const selectedCluster: Cluster = this.clustersService.getClusters()[parseInt(this.form.value.cluster)];
    const password: string = this.form.value.password;

    this.isLoading = true;
    this.form.disable();

    this.hazelcastService.sendAuthenticate(selectedCluster.instanceName, password).then((response: AuthenticateResponseDTO) => {
      this.isLoading = false;
      this.form.enable();

      if (response.error !== null) {
        this.snackbarService.show(`Error while authenticating: ${response.error}`);
      } else {
        this.clustersService.setCurrentCluster(selectedCluster);
        this.router.navigateByUrl('/dashboard');
      }
    }).catch((error: ErrorMessageDTO) => {
      this.isLoading = false;
      this.form.enable();
      this.snackbarService.show(`Error while authenticating: ${error.errors.join('\n')}`);
    });
  }
}
