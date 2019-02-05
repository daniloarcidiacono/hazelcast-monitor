import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {MdcIconModule, MdcTextFieldModule, MdcTypographyModule} from "@angular-mdc/web";
import {SharedSearchBarComponent} from "@shared/components/search-bar/shared-search-bar.component";

@NgModule({
  declarations: [
    SharedSearchBarComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,

    // Angular MDC Web
    MdcTypographyModule,
    MdcIconModule,
    MdcTextFieldModule
  ],
  exports: [
    SharedSearchBarComponent
  ]
})
export class SharedSearchBarModule { }
