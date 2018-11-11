import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';

export interface FrequencyOption {
  caption: string;
  frequency: number;
}

@Component({
  selector: 'shared-frequency-button',
  templateUrl: './shared-frequency-button.component.html',
  styleUrls: [ './shared-frequency-button.component.scss' ]
})
export class SharedFrequencyButtonComponent implements OnChanges {
  @Input()
  private frequency: number;

  @Output()
  private frequencyChange: EventEmitter<number> = new EventEmitter<number>();

  // Bindings
  public options: FrequencyOption[] = [
    {
      caption: 'Manual',
      frequency: 0
    },
    {
      caption: '1 second',
      frequency: 1
    },
    {
      caption: '5 seconds',
      frequency: 5
    },
    {
      caption: '10 seconds',
      frequency: 10
    },
    {
      caption: '30 seconds',
      frequency: 30
    },
    {
      caption: '1 minute',
      frequency: 60
    },
    {
      caption: '5 minutes',
      frequency: 300
    },
    {
      caption: '10 minutes',
      frequency: 600
    }
  ];

  public caption: string;

  public constructor() {
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (!!changes['frequency'] && changes['frequency'].currentValue !== changes['frequency'].previousValue) {
      this.caption = `${this.frequency} seconds`;

      // Search for an existing option label
      for (let i = 0; i < this.options.length; i++) {
        if (this.options[i].frequency === this.frequency) {
          this.caption = this.options[i].caption;
          break;
        }
      }
    }
  }

  public setFrequency(option: FrequencyOption) {
    if (this.frequency !== option.frequency) {
      this.frequency = option.frequency;
      this.caption = option.caption;
      this.frequencyChange.emit(this.frequency);
    }
  }
}
