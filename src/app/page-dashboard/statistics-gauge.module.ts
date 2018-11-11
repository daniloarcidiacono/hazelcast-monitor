import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {StatisticsGaugeComponent} from './statistics-gauge.component';
import {PercentPipe} from '@angular/common';
import {SharedPipesModule} from '@shared/pipes/shared-pipes.module';
import {SharedBytesPipe} from '@shared/pipes/shared-bytes.pipe';

@NgModule({
  declarations: [
    StatisticsGaugeComponent
  ],
  imports: [
    BrowserModule,

    // Shared
    SharedPipesModule
  ],
  exports: [
    StatisticsGaugeComponent
  ],
  providers: [
    PercentPipe,
    SharedBytesPipe
  ]
})
export class StatisticsGaugeModule { }
