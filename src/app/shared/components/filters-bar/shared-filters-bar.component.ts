import {Component, Input, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {
  ErrorMessageDTO, SubscriptionNoticeResponseDTO,
  UpdateSubscriptionRequestDTO, UpdateSubscriptionResponseDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {Subscription} from 'rxjs/index';
import {FiltersProductDTO} from '@shared/dto/topic-products.dto';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'shared-filters-bar',
  templateUrl: './shared-filters-bar.component.html',
  styleUrls: [ './shared-filters-bar.component.scss' ]
})
export class SharedFiltersBarComponent implements OnDestroy {
  @Input()
  private subscriptionId: number;

  @Input()
  private showEditor: boolean;

  private editorOpened: boolean = false;
  private filters: string[];
  private dataSub: Subscription;
  private form: FormGroup;

  // View bindings
  public bindings: any = {
    ctrl: this,

    get editorOpened(): boolean {
      return this.ctrl.editorOpened;
    },

    get showEditor(): boolean {
      return this.ctrl.showEditor;
    },

    get form(): FormGroup {
      return this.ctrl.form;
    },

    get filters(): string[] {
      return this.ctrl.filters;
    }
  };

  public constructor(private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private fb: FormBuilder) {
    this.initForm();
    this.subscribe();
  }

  public ngOnDestroy(): void {
    this.unsubscribe();
  }

  public toggleEditor(): void {
    this.editorOpened = !this.editorOpened;
  }

  public apply(): void {
    const request: UpdateSubscriptionRequestDTO = {
      messageType: 'update_subscription',
      subscriptionId: this.subscriptionId,
      parameter: 'filter',
      value: this.form.value.filter
    };

    this.hazelcastService.sendUpdateSubscription(request).then((response: UpdateSubscriptionResponseDTO) => {
      if (!response.error) {
        this.snackbarService.show(`Error while update the filter ${response.error}`);
      } else {
        this.snackbarService.show('Filter updated');
      }
    }).catch((error: ErrorMessageDTO) => {
      this.snackbarService.show(`Compile error: ${error.errors.join('\n')}`);
    });
  }

  public isApplyButtonEnabled(): boolean {
    return this.subscriptionId !== undefined;
  }

  private initForm(): void {
    this.form = this.fb.group({
      filter: [ undefined, Validators.required ]
    });
  }

  private subscribe(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToFilters().subscribe(
        (notice: SubscriptionNoticeResponseDTO<FiltersProductDTO>) => {
          console.log(notice);
          this.filters = notice.notice.filters;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the filters: ${error.errors}`);
        }
      );
    }
  }

  private unsubscribe(): void {
    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
      this.dataSub = undefined;
    }
  }
}
