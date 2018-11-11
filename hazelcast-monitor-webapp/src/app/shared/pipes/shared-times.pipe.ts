import {PipeTransform, Pipe} from '@angular/core';

/**
 * https://stackoverflow.com/questions/36535629/repeat-html-element-multiple-times-using-ngfor-based-on-a-number/36535705
 */
@Pipe({name: 'times'})
export class SharedTimesPipe implements PipeTransform {
  transform(value: number): any {
    const iterable = {};
    iterable[Symbol.iterator] = function* () {
      let n = 0;
      while (n < value) {
        yield n++;
      }
    };

    return iterable;
  }
}
