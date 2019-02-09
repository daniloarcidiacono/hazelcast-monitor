package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executor service that can be used to schedule threads with variable delay, following a Poisson process.
 */
public class PoissonExecutorService {
    private final ScheduledExecutorService executorService;

    public PoissonExecutorService(final ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Schedules command as a Poisson process having eventCount events per unit time.
     * @param command the command to schedule
     * @param eventCount total number of events
     * @param unit the unit of time
     */
    public PoissonRunnableWrapper scheduleAsPoissonProcess(final Runnable command, final long eventCount, final TimeUnit unit) {
        // Calculate the Poisson rate in milliseconds
        final double lambda = (double)eventCount / unit.toMillis(1);
        final PoissonRunnableWrapper poissonRunnable = new PoissonRunnableWrapper(lambda, command, executorService);

        // First schedule
        executorService.schedule(
            poissonRunnable,
            (long)Math.floor(PoissonRunnableWrapper.nextTime(lambda)),
            TimeUnit.MILLISECONDS
        );

        return poissonRunnable;
    }
}
