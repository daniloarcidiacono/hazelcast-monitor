import {NgModule} from '@angular/core';
import {SharedTimesPipe} from '@shared/pipes/shared-times.pipe';
import {SharedSafePipe} from '@shared/pipes/shared-safe.pipe';
import {SharedBytesPipe} from '@shared/pipes/shared-bytes.pipe';
import {SharedOptionalValuePipe} from "@shared/pipes/shared-optionalvalue.pipe";

@NgModule({
  declarations: [
    SharedTimesPipe,
    SharedSafePipe,
    SharedBytesPipe,
    SharedOptionalValuePipe
  ],
  imports: [
  ],
  exports: [
    SharedTimesPipe,
    SharedSafePipe,
    SharedBytesPipe,
    SharedOptionalValuePipe
  ],
  providers: [
  ]
})
export class SharedPipesModule {
}
