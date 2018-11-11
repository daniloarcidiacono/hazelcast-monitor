import {Component, HostBinding} from '@angular/core';

@Component({
  templateUrl: './page-notfound.component.html',
  styleUrls: ['./page-notfound.component.scss']
})
export class PageNotFoundComponent {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';

  public constructor() {
  }
}
