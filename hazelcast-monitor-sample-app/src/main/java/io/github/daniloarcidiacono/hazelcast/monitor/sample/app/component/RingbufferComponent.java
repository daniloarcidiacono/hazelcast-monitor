package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.ringbuffer.Ringbuffer;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for ringbuffers.
 * @see Ringbuffer
 */
public class RingbufferComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RingbufferComponent.class);
    private Ringbuffer<Object> ringbuffer;

    public RingbufferComponent(final HazelcastInstance hazelcastInstance) {
        ringbuffer = hazelcastInstance.getRingbuffer("test_ringbuffer");

        for (final Person person : Persons.random(4)) {
            ringbuffer.add(person);
        }
    }

    @Override
    public void run() {
    }
}
