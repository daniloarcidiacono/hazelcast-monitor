import {Component, Input} from '@angular/core';

@Component({
  selector: 'section-card',
  templateUrl: './section-card.component.html',
  styleUrls: ['./section-card.component.scss']
})
export class SectionCardComponent {
  @Input()
  public counter: number;

  @Input()
  public title: string;

  @Input()
  public fontSet: string;

  @Input()
  public fontIcon: string;

  @Input()
  public color: string;

  public constructor() {
  }
}
