import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {MdcButtonModule, MdcIconModule, MdcSnackbarModule, MdcTopAppBarModule} from '@angular-mdc/web';
import {RouterModule} from '@angular/router';
import {PageClustersModule} from './page-clusters/page-clusters.module';
import {PageDashboardModule} from './page-dashboard/page-dashboard.module';
import {PageNotFoundModule} from './page-notfound/page-notfound.module';
import {PageConnectModule} from './page-connect/page-connect.module';
import {AppRoutingModule} from './app-routing.module';
import {SharedConnectionIndicatorModule} from '@shared/components/connection-indicator/shared-connection-indicator.module';
import {PageTestModule} from './page-test/page-test.module';
import {MonacoEditorModule} from 'ngx-monaco-editor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,

    // Angular MDC Web
    MdcTopAppBarModule,
    MdcButtonModule,
    MdcIconModule,
    MdcSnackbarModule,

    // Application sections
    PageClustersModule,
    PageConnectModule,
    PageDashboardModule,
    PageNotFoundModule,
    PageTestModule,

    // use forRoot() in main app module only.
    MonacoEditorModule.forRoot(),

    // Shared
    SharedConnectionIndicatorModule,

    // Routing
    RouterModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
