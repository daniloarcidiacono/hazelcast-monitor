import {NgModule} from '@angular/core';
import {SharedTimesPipe} from '@shared/pipes/shared-times.pipe';
import {SharedSafePipe} from "@shared/pipes/shared-safe.pipe";

@NgModule({
  declarations: [
    SharedTimesPipe,
    SharedSafePipe
  ],
  imports: [
  ],
  exports: [
    SharedTimesPipe,
    SharedSafePipe
  ],
  providers: [
  ]
})
export class SharedPipesModule {
}
