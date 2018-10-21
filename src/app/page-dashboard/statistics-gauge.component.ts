import {Component, Input, OnChanges} from '@angular/core';
import {PercentPipe} from "@angular/common";
import {SharedBytesPipe} from "@shared/pipes/shared-bytes.pipe";

@Component({
  selector: 'statistics-gauge',
  templateUrl: './statistics-gauge.component.html',
  styleUrls: ['./statistics-gauge.component.scss']
})
export class StatisticsGaugeComponent implements OnChanges {
  @Input()
  public label: string;

  @Input()
  public percentage: boolean;

  @Input()
  public bytes: boolean;

  @Input()
  public units: string;

  @Input()
  public color: string;

  @Input()
  public value: number;

  @Input()
  public min: number;

  @Input()
  public max: number;

  public multi: any;
  public colorScheme: any;

  public constructor(private percentPipe: PercentPipe,
                     private bytesPipe: SharedBytesPipe) {
  }

  public ngOnChanges(): void {
    this.multi = [
      {
        name: 'Data',
        value: this.value
      }
    ];

    this.colorScheme = {
      domain: [
        this.color
      ]
    };
  }

  public valueFormattingFn = (current: number): string => {
    if (this.percentage) {
      return this.percentPipe.transform(current);
    }

    if (this.bytes) {
      return this.bytesPipe.transform(current, 2);
    }

    return `${this.value}`;
  }
}
