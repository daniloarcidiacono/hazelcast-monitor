package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper of {@link Runnable} that automatically reschedules itself following a Poisson distribution.
 * @see PoissonExecutorService
 */
public class PoissonRunnableWrapper implements Runnable {
    /**
     * Poisson rate (number of events per milliseconds)
     */
    private double lambda;

    /**
     * Original runnable
     */
    private final Runnable runnable;

    /**
     * Scheduled executor
     */
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Flag used to stop the runnable
     */
    private volatile boolean stopped = false;

    public PoissonRunnableWrapper(final double lambda, final Runnable runnable, final ScheduledExecutorService scheduledExecutorService) {
        this.lambda = lambda;
        this.runnable = runnable;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * Computes the waiting time for the next event in a poisson process.
     * @param lambda the number of events per unit time
     * @return the time for next event in a poisson process
     */
    public static double nextTime(final double lambda) {
        // The interarrival times in a poisson process follows the exponential distribution.
        // We sample the exponential distribution by inverting the cumulative distribution function.
        // u in (0; 1] (we can't pass 0 to the logarithm)
        final double u = 1 - Math.random();
        return -Math.log(u) / lambda;
    }

    /**
     * Stops the automatic rescheduling of the runnable.
     */
    public void stop() {
        stopped = true;
    }

    @Override
    public void run() {
        runnable.run();

        // Reschedule
        if (!stopped) {
            scheduledExecutorService.schedule(
                this,
               (long) Math.floor(nextTime(lambda)),
                TimeUnit.MILLISECONDS
            );
        }
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
