import {Component, ElementRef, Input, Renderer} from "@angular/core";

@Component({
  selector: 'shared-fullscreen-button',
  templateUrl: './shared-fullscreen-button.component.html',
  styleUrls: [ './shared-fullscreen-button.component.scss' ]
})
export class SharedFullscreenButtonComponent {
  @Input()
  public title: string = "Toggle fullscreen";

  @Input()
  private element: ElementRef;

  public fullscreen: boolean = false;

  public constructor(private renderer: Renderer) {
  }

  public toggleFullscreen(): void {
    this.fullscreen = !this.fullscreen;

    // [ngClass]="{ 'Element--fullscreen': fullscreen }"
    this.renderer.setElementClass(this.element, 'Element--fullscreen', this.fullscreen);
  }
}
