package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import com.jayway.jsonpath.JsonPath;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.exception.UpdateParameterException;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.TopicProducerFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.MembersProduct;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.TopicProduct;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.query.FalsePredicate;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.query.ScriptPredicate;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.utils.JsonPathUtils;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.query.TruePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * Dedicated producer for Hazelcast Topics.
 * This producer emits messages spontaneously, therefore is not intended to be wrapped by
 * {@link ScheduledTopicProducer}.
 *
 * @see TopicProducerFactory#instanceTopicProducer(SubscribeRequest)
 * @author Danilo Arcidiacono
 */
public class TopicTopicProducer extends AbstractTopicProducer implements MessageListener {
    public static final String TOPIC_TYPE = "distributed_object_details";
    private static final Logger logger = LoggerFactory.getLogger(TopicTopicProducer.class);
    private HazelcastInstance instance;
    private ITopic topic;
    private String registrationId;

    // Query
    private Predicate predicate = TruePredicate.INSTANCE;
    private JsonPath jsonPath = null;

    public TopicTopicProducer(final String instanceName, final String topicName) {
        super(TOPIC_TYPE, instanceName);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
        topic = instance.getTopic(topicName);
    }

    @Override
    public void start() {
        if (registrationId == null) {
            registrationId = topic.addMessageListener(this);
        }
    }

    @Override
    public void stop() {
        if (registrationId != null) {
            topic.removeMessageListener(registrationId);
        }
    }

    @Override
    public void updateParameter(final String parameter, final String value) throws UpdateParameterException {
        switch (parameter) {
            case "filter":
                try {
                    predicate = new ScriptPredicate(value);
                } catch (Exception e) {
                    predicate = FalsePredicate.INSTANCE;
                    throw new UpdateParameterException("Error while updating the filter", e);
                }

                break;

            case "jsonPath":
                try {
                    jsonPath = JsonPathUtils.toJsonPath(value);
                } catch (Exception e) {
                    throw new UpdateParameterException("Error while updating the slice", e);
                }

                break;

            default:
                super.updateParameter(parameter, value);
                break;
        }
    }

    @Override
    public TopicProduct produce() {
        return new TopicProduct();
    }

    @Override
    public void onMessage(final Message message) {
        // Filter
        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        if (!predicate.test(message.getMessageObject())) {
            return;
        }

        // Slice
        final Object sliced = JsonPathUtils.slice(message.getMessageObject(), jsonPath);

        // If we have applied the slice with success
        if (sliced != null) {
            // Produce
            final TopicProduct product = produce();
            product.setMessage(sliced);
            product.setMessageString(sliced.toString());
            product.setPublisher(
                new MembersProduct.MemberSummary(
                    message.getPublishingMember().getAddress().getHost(),
                    message.getPublishingMember().getAddress().getPort(),
                    message.getPublishingMember().getVersion().toString(),
                    message.getPublishingMember().getUuid()
                )
            );
            product.setPublishTime(message.getPublishTime());

            // Send the product to the client
            notice(product);
        }
    }
}
