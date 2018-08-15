package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.factory.TopicProducerFactory;
import it.xdnl.hazelcast.monitor.agent.product.MembersProduct;
import it.xdnl.hazelcast.monitor.agent.product.TopicProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public TopicTopicProducer(final String instanceName, final String topicName) {
        super(TOPIC_TYPE);
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
    public TopicProduct produce() {
        return new TopicProduct();
    }

    @Override
    public void onMessage(final Message message) {
        final TopicProduct product = produce();
        product.setMessage(message.getMessageObject());
        product.setMessageString(message.getMessageObject().toString());
        product.setPublisher(new MembersProduct.MemberSummary(
            message.getPublishingMember().getAddress().getHost(),
            message.getPublishingMember().getAddress().getPort(),
            message.getPublishingMember().getVersion().toString(),
            message.getPublishingMember().getUuid()
        ));
        product.setPublishTime(message.getPublishTime());

        notice(product);
    }
}
