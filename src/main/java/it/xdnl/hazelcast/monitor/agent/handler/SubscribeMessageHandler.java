package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.ErrorMessage;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.request.UnsubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.response.SubscribeResponse;
import it.xdnl.hazelcast.monitor.agent.factory.TopicProducerFactory;
import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;
import it.xdnl.hazelcast.monitor.agent.producer.AbstractTopicProducer;

public class SubscribeMessageHandler implements MessageHandler {
    private ConnectionSubscriptionsRegistry registry = new ConnectionSubscriptionsRegistry();
    private TopicProducerFactory topicProducerFactory;

    public SubscribeMessageHandler(final TopicProducerFactory topicProducerFactory) {
        this.topicProducerFactory = topicProducerFactory;
    }

    @Override
    public void process(final ClientConnection connection, final AbstractMessage genericMessage) {
        if (genericMessage instanceof SubscribeRequest) {
            onSubscribeMessage(connection, (SubscribeRequest) genericMessage);
        }

        if (genericMessage instanceof UnsubscribeRequest) {
            onUnsubscribeMessage(connection, (UnsubscribeRequest) genericMessage);
        }
    }

    private void onUnsubscribeMessage(final ClientConnection connection, final UnsubscribeRequest request) {
        registry.unsubscribe(request.getSubscriptionId());
    }

    private void onSubscribeMessage(final ClientConnection connection, final SubscribeRequest request) {
        final AbstractTopicProducer topic = topicProducerFactory.instanceTopicProducer(request);
        if (topic != null) {
            // Subscribe
            final long subscriptionId = registry.subscribe(connection, topic);

            // Notify the user
            ClientConnectionUtils.convertAndReply(connection, request, new SubscribeResponse(
                request.getTopic(),
                subscriptionId
            ));

            // Begin the topic
            // This must be done after we send the response to the client, to ensure that
            // no topic updates are received before the subscription confirmation.
            topic.start();
        } else {
            ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage("Unrecognized topic"));
        }
    }

    @Override
    public boolean supports(final AbstractMessage message) {
        return (message instanceof SubscribeRequest) || (message instanceof UnsubscribeRequest);
    }
}
