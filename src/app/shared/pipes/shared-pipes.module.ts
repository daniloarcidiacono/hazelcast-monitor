import {NgModule} from '@angular/core';
import {SharedTimesPipe} from '@shared/pipes/shared-times.pipe';
import {SharedSafePipe} from '@shared/pipes/shared-safe.pipe';
import {SharedBytesPipe} from '@shared/pipes/shared-bytes.pipe';

@NgModule({
  declarations: [
    SharedTimesPipe,
    SharedSafePipe,
    SharedBytesPipe
  ],
  imports: [
  ],
  exports: [
    SharedTimesPipe,
    SharedSafePipe,
    SharedBytesPipe
  ],
  providers: [
  ]
})
export class SharedPipesModule {
}
