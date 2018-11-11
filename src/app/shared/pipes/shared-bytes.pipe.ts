import { Pipe, PipeTransform } from '@angular/core';
export type ByteUnit = 'B' | 'KB' | 'MB' | 'GB' | 'TB';

@Pipe({
  name: 'bytes'
})
export class SharedBytesPipe implements PipeTransform {
  static formats: { [key: string]: { max: number, prev?: ByteUnit } } = {
    'B': { max: 1024 },
    'KB': {max: Math.pow(1024, 2), prev: 'B'},
    'MB': {max: Math.pow(1024, 3), prev: 'KB'},
    'GB': {max: Math.pow(1024, 4), prev: 'MB'},
    'TB': {max: Number.MAX_SAFE_INTEGER, prev: 'GB'}
  };

  public transform(input: any, decimal: number = 0, from: ByteUnit = 'B', to?: ByteUnit): any {
    if (!(this.isNumberFinite(input) && this.isNumberFinite(decimal) &&
        this.isInteger(decimal) && this.isPositive(decimal))) {
      return input;
    }

    let bytes = input;
    let unit = from;
    while (unit !== 'B') {
      bytes *= 1024;
      unit = SharedBytesPipe.formats[unit].prev!;
    }

    if (to) {
      const format = SharedBytesPipe.formats[to];
      const result = this.toDecimal(SharedBytesPipe.calculateResult(format, bytes), decimal);

      return SharedBytesPipe.formatResult(result, to);
    }

    for (const key in SharedBytesPipe.formats) {
      const format = SharedBytesPipe.formats[key];
      if (bytes < format.max) {
        const result = this.toDecimal(SharedBytesPipe.calculateResult(format, bytes), decimal);
        return SharedBytesPipe.formatResult(result, key);
      }
    }
  }

  static formatResult(result: number, unit: string): string {
    return `${result} ${unit}`;
  }

  static calculateResult(format: { max: number, prev?: ByteUnit }, bytes: number) {
    const prev = format.prev ? SharedBytesPipe.formats[format.prev] : undefined;
    return prev ? bytes / prev.max : bytes;
  }

  private isNumber(value: any): value is number {
    return typeof value === 'number';
  }

  private isNumberFinite(value: any): value is number {
    return this.isNumber(value) && isFinite(value);
  }

  private isPositive(value: number): boolean {
    return value >= 0;
  }

  private isInteger(value: number): boolean {
    return (value % 1) === 0;
  }

  private toDecimal(value: number, decimal: number): number {
    return Math.round(value * Math.pow(10, decimal)) / Math.pow(10, decimal);
  }
}
