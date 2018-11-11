import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {MdcButtonModule, MdcIconButtonModule, MdcIconModule, MdcTabBarModule} from '@angular-mdc/web';
import {SharedDynamicTabsComponent} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.component';
import {TabHostDirective} from '@shared/components/dynamic-tabs/tab-host.directive';

@NgModule({
  declarations: [
    SharedDynamicTabsComponent,
    TabHostDirective
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTabBarModule,
    MdcButtonModule,
    MdcIconModule,
    MdcIconButtonModule
  ],
  exports: [
    SharedDynamicTabsComponent
  ]
})
export class SharedDynamicTabsModule { }
