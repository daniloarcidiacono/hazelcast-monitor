import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[tab-host]'
})
export class TabHostDirective {
  public constructor(public viewContainerRef: ViewContainerRef) {
  }
}
