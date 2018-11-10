import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'safeRound'
})
export class SharedSafeRoundPipe implements PipeTransform {
  public transform(input: any): any {
    if (input !== null && input !== undefined) {
      return Math.round(input);
    }

    return input;
  }
}
