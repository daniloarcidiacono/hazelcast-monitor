import {
  Component,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  ViewChild,
  ViewContainerRef
} from '@angular/core';
import {TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {TabHostDirective} from '@shared/components/dynamic-tabs/tab-host.directive';
import {MdcTabBar} from '@angular-mdc/web';

@Component({
  selector: 'shared-dynamic-tabs',
  templateUrl: './shared-dynamic-tabs.component.html',
  styleUrls: [ './shared-dynamic-tabs.component.scss' ]
})
export class SharedDynamicTabsComponent {
  public tabs: TabData[] = [
  ];

  public activeTabIndex: number = -1;

  @ViewChild(TabHostDirective)
  private tabHost: TabHostDirective;

  @ViewChild(MdcTabBar)
  private tabBarComponent: MdcTabBar;

  public constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public addTab(data: TabData): void {
    const componentFactory: ComponentFactory<any> = this.componentFactoryResolver.resolveComponentFactory(data.componentClass);
    const viewContainerRef: ViewContainerRef = this.tabHost.viewContainerRef;
    const componentRef: ComponentRef<any> = viewContainerRef.createComponent(componentFactory);
    for (const inputName in data.componentInputs) {
      if (data.componentInputs.hasOwnProperty(inputName)) {
        componentRef.instance[inputName] = data.componentInputs[inputName];
      }
    }

    data.componentRef = componentRef;
    this.tabs.push(data);

    // Inject the tab data into the component
    if (!!data.componentRef.instance['tabCreated']) {
      data.componentRef.instance.tabCreated(data);
    }

    this.selectTab(this.tabs.length - 1);
  }

  private selectTab(tabIndex: number): void {
    if (this.activeTabIndex !== tabIndex) {
      this.deactivateTab(this.activeTabIndex);
      this.activateTab(tabIndex);
      this.activeTabIndex = tabIndex;
    }
  }

  public closeTab(tabIndex: number): void {
    // Deactivate the tab
    this.deactivateTab(tabIndex);

    // Remove the tab
    this.tabs[tabIndex].componentRef.destroy();
    this.tabs[tabIndex].componentRef = undefined;
    this.tabs[tabIndex].active = false;
    this.tabs.splice(tabIndex, 1);

    // If we have removed a tab preceding the active tab
    if (tabIndex <= this.activeTabIndex) {
      // Just shift to the precedent tab (or none if there are no more tabs)
      this.activeTabIndex--;

      // If we have more tabs, select the first
      if (this.activeTabIndex === -1 && this.tabs.length > 0) {
        this.activeTabIndex = 0;
      }

      this.activateTab(this.activeTabIndex);
    }
  }

  private deactivateTab(tabIndex: number): void {
    if (tabIndex >= 0 && tabIndex < this.tabs.length) {
      if (!!this.tabs[tabIndex].componentRef.instance['beforeHide']) {
        this.tabs[tabIndex].componentRef.instance.beforeHide();
      }

      this.tabs[tabIndex].componentRef.location.nativeElement.hidden = true;
      this.tabs[tabIndex].active = false;
    }
  }

  private activateTab(tabIndex: number): void {
    if (tabIndex >= 0 && tabIndex < this.tabs.length) {
      if (!!this.tabs[tabIndex].componentRef.instance['beforeShow']) {
        this.tabs[tabIndex].componentRef.instance.beforeShow();
      }

      this.tabs[tabIndex].componentRef.location.nativeElement.hidden = false;
      this.tabs[tabIndex].active = true;
    }
  }
}
