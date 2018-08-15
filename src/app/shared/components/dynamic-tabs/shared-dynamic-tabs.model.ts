import {ComponentRef, Type} from '@angular/core';

export interface TabData {
  label: string;
  active?: boolean;
  recording?: boolean;
  componentClass?: Type<any>;
  componentInputs?: { [ name: string ]: any };

  // Initialized by SharedDynamicTabsComponent
  componentRef?: ComponentRef<any>;
}

export interface TabAwareComponent {
  beforeShow(): void;
  beforeHide(): void;
  tabCreated(tab: TabData): void;
}
