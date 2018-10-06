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

  public logTab(tabIndex: number): void {
    console.log(tabIndex);
  }

  private selectTab(tabIndex: number): void {
    const currentTab: number = this.tabBarComponent.getActiveTabIndex();
    if (currentTab !== tabIndex) {
      if (currentTab >= 0 && currentTab < this.tabs.length) {
        if (!!this.tabs[currentTab].componentRef.instance['beforeHide']) {
          this.tabs[currentTab].componentRef.instance.beforeHide();
        }

        this.tabs[currentTab].componentRef.location.nativeElement.hidden = true;
        this.tabs[currentTab].active = false;
      }

      if (tabIndex >= 0 && tabIndex < this.tabs.length) {
        if (!!this.tabs[tabIndex].componentRef.instance['beforeShow']) {
          this.tabs[tabIndex].componentRef.instance.beforeShow();
        }
        this.tabs[tabIndex].componentRef.location.nativeElement.hidden = false;
        this.tabs[tabIndex].active = true;
      }
    }

    this.activeTabIndex = tabIndex;
  }

  public closeTab(tabIndex: number): void {
    if (tabIndex + 1 < this.tabs.length) {
      this.selectTab(tabIndex + 1);
    } else if (tabIndex - 1 >= 0) {
      this.selectTab(tabIndex - 1);
    } else {
      this.selectTab(-1);
    }

    this.tabs[tabIndex].componentRef.destroy();
    this.tabs[tabIndex].componentRef = undefined;
    this.tabs[tabIndex].active = false;
    this.tabs.splice(tabIndex, 1);
  }
}
