import {ComponentRef, Type} from '@angular/core';
import {FontIcon} from "@shared/constants/shared-page-icons.constants";

export interface TabData {
  icon?: FontIcon;
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
