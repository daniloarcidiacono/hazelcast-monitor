import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {MdcIconModule, MdcTabModule} from '@angular-mdc/web';
import {SharedDynamicTabsComponent} from "@shared/components/dynamic-tabs/shared-dynamic-tabs.component";
import {TabHostDirective} from "@shared/components/dynamic-tabs/tab-host.directive";

@NgModule({
  declarations: [
    SharedDynamicTabsComponent,
    TabHostDirective
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTabModule,
    MdcIconModule
  ],
  exports: [
    SharedDynamicTabsComponent
  ]
})
export class SharedDynamicTabsModule { }
