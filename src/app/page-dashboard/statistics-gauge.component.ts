import {Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PercentPipe} from '@angular/common';
import {SharedBytesPipe} from '@shared/pipes/shared-bytes.pipe';
import {Chart} from 'chart.js';

@Component({
  selector: 'statistics-gauge',
  templateUrl: './statistics-gauge.component.html',
  styleUrls: ['./statistics-gauge.component.scss']
})
export class StatisticsGaugeComponent implements OnInit, OnChanges, OnDestroy {
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

  // Chart objects
  private chartGauge: Chart;

  // Chart DOM hooks
  @ViewChild('chartGauge')
  private chartGaugeElementRef: ElementRef<HTMLCanvasElement>;

  public data: number[] = [0, 0];
  public colorScheme: string[] = [];


  public constructor(private percentPipe: PercentPipe,
                     private bytesPipe: SharedBytesPipe) {
  }

  public ngOnInit(): void {
    this.chartGauge = new Chart(this.chartGaugeElementRef.nativeElement, {
      type: 'doughnut',
      data: {
        labels: [ 'Used', 'Not used' ],
        datasets: [
          {
            data: this.data,
            backgroundColor: this.colorScheme,
            label: 'Receives'
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        cutoutPercentage: 80,
        legend: {
          display: false
        },
        title: {
          text: this.label,
          position: 'bottom',
          fontColor: this.color,
          display: false
        },
        tooltips: {
          callbacks: {
            label: (item, data) => {
              return `${data.labels[item.index]}: ${this.valueFormattingFn(data.datasets[item.datasetIndex].data[item.index])}`;
            }
          }
        }
      }
    });
  }

  public ngOnDestroy(): void {
    this.chartGauge.destroy();
  }

  public ngOnChanges(): void {
    this.data[0] = this.value;
    this.data[1] = this.max - this.value;
    this.colorScheme = [ this.color, '#7f7f7f'];
    this.chartGauge.options.title.text = this.label;
    this.chartGauge.options.title.fontColor = this.color;

    // Update
    this.chartGauge.update();
  }

  public gaugeLabel(): string {
    if (this.bytes) {
      return `${this.valueFormattingFn(this.value)} of ${this.valueFormattingFn(this.max)}`;
    }

    return this.valueFormattingFn(this.value);
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
