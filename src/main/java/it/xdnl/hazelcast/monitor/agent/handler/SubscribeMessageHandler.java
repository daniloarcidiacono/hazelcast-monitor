package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.ErrorMessage;
import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.request.UnsubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.request.UpdateSubscriptionRequest;
import it.xdnl.hazelcast.monitor.agent.dto.response.SubscribeResponse;
import it.xdnl.hazelcast.monitor.agent.dto.response.UpdateSubscriptionResponse;
import it.xdnl.hazelcast.monitor.agent.exception.UpdateParameterException;
import it.xdnl.hazelcast.monitor.agent.factory.TopicProducerFactory;
import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.producer.AbstractTopicProducer;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;

public class SubscribeMessageHandler implements MessageHandler {
    private ConnectionSubscriptionsRegistry subscriptionsRegistry;
    private TopicProducerFactory topicProducerFactory;

    public SubscribeMessageHandler() {
    }

    @Override
    public void process(final ClientConnection connection, final AbstractMessage genericMessage) {
        if (genericMessage instanceof SubscribeRequest) {
            onSubscribeMessage(connection, (SubscribeRequest)genericMessage);
        }

        if (genericMessage instanceof UpdateSubscriptionRequest) {
            onUpdateSubscription(connection, (UpdateSubscriptionRequest)genericMessage);
        }

        if (genericMessage instanceof UnsubscribeRequest) {
            onUnsubscribeMessage(connection, (UnsubscribeRequest)genericMessage);
        }
    }

    private void onUnsubscribeMessage(final ClientConnection connection, final UnsubscribeRequest request) {
        subscriptionsRegistry.unsubscribe(request.getSubscriptionId());
    }

    private void onSubscribeMessage(final ClientConnection connection, final SubscribeRequest request) {
        final AbstractTopicProducer topic = topicProducerFactory.instanceTopicProducer(request);
        if (topic != null) {
            // Subscribe
            final long subscriptionId = subscriptionsRegistry.subscribe(connection, topic);

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

    private void onUpdateSubscription(final ClientConnection connection, final UpdateSubscriptionRequest request) {
        final AbstractTopicProducer topic = subscriptionsRegistry.getTopicProducer(request.getSubscriptionId());
        if (topic != null) {
            try {
                topic.updateParameter(request.getParameter(), request.getValue());
            } catch (UpdateParameterException ex) {
                final UpdateSubscriptionResponse response = new UpdateSubscriptionResponse();
                response.setError(ex.getMessage());
                response.setParameter(ex.getParameterName());
                response.setValue(ex.getActualValue());

                ClientConnectionUtils.convertAndReply(connection, request, response);
            }
        } else {
            ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage("Subscription not found"));
        }
    }

    @Override
    public boolean supports(final AbstractMessage message) {
        return (message instanceof SubscribeRequest) ||
               (message instanceof UnsubscribeRequest) ||
               (message instanceof UpdateSubscriptionRequest);
    }

    public ConnectionSubscriptionsRegistry getSubscriptionsRegistry() {
        return subscriptionsRegistry;
    }

    public void setSubscriptionsRegistry(ConnectionSubscriptionsRegistry subscriptionsRegistry) {
        this.subscriptionsRegistry = subscriptionsRegistry;
    }

    public TopicProducerFactory getTopicProducerFactory() {
        return topicProducerFactory;
    }

    public void setTopicProducerFactory(TopicProducerFactory topicProducerFactory) {
        this.topicProducerFactory = topicProducerFactory;
    }
}
