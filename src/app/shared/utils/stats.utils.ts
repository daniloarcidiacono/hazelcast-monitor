import {Observable, Observer, Subscription} from "rxjs/index";

export type LineChartData = { x: any, y: number }[];
export type BarChartData = number[];

export abstract class StatisticsUtils {
  // Copies src to dst
  public static updateArray(dst: any[], src: any[]) {
    let i = 0;
    while (i < src.length) {
      if (i < dst.length) {
        // Update
        dst[i] = src[i];
      } else {
        // Mutate
        dst.push(src[i]);
      }
      i++;
    }

    // Clip eventual excess items of dst
    dst.length = src.length;
  }

  public static derivative(source: Observable<number[]>): Observable<any> {
    return new Observable<any>((observer: Observer<number[]>) => {
      const sampleBuffer: number[][] = [];

      const sub: Subscription = source.subscribe(
        (value: number[]) => {

          // Gather samples
          sampleBuffer.push(value);

          // If we have enough to calculate the derivative
          if (sampleBuffer.length === 2) {
            const dt: number = (sampleBuffer[1][0] - sampleBuffer[0][0]);

            observer.next(
              [
                sampleBuffer[0][0],
                (sampleBuffer[1][1] - sampleBuffer[0][1]) / dt
              ]
            );

            // Remove the oldest sample
            sampleBuffer.shift();
          }
        },
        (error: any) => {
          observer.error(error);
        },
        () => {
          observer.complete();
        }
      );

      return () => {
        sub.unsubscribe();
      };
    });
  }
}
