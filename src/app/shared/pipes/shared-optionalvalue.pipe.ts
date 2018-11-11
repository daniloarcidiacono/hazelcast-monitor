import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'optionalValue'
})
export class SharedOptionalValuePipe implements PipeTransform {
  public transform(input: any, missingLabel: string = 'n.a.'): any {
    if (input !== null && input !== undefined && !isNaN(input)) {
      return input;
    }

    return missingLabel;
  }
}
