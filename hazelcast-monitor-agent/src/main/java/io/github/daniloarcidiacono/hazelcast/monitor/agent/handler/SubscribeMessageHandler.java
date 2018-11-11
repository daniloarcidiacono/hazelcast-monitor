package io.github.daniloarcidiacono.hazelcast.monitor.agent.handler;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.ErrorMessage;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.UnsubscribeRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response.SubscribeResponse;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.TopicProducerFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.producer.AbstractTopicProducer;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.PullSubscriptionRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.UpdateSubscriptionRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response.UpdateSubscriptionResponse;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.exception.UpdateParameterException;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.producer.ScheduledTopicProducer;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.utils.ClientConnectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        if (genericMessage instanceof PullSubscriptionRequest) {
            onPullSubscriptionRequest(connection, (PullSubscriptionRequest)genericMessage);
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

            // Inject the parameters
            if (request.getParameters() != null) {
                for (Map.Entry<String, String> entry : request.getParameters().entrySet()) {
                    try {
                        topic.updateParameter(entry.getKey(), entry.getValue());
                    } catch (UpdateParameterException e) {
                        // Just ignore
                    }
                }
            }

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
            final Map<String, String> actualParameters = new HashMap<>();
            final List<String> failedParameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : request.getParameters().entrySet()) {
                try {
                    topic.updateParameter(entry.getKey(), entry.getValue());
                    actualParameters.put(entry.getKey(), entry.getValue());
                } catch (UpdateParameterException e) {
                    // Just ignore
                    actualParameters.put(e.getParameterName(), e.getActualValue());
                    failedParameters.add(e.getParameterName());
                }
            }

            // Reply
            final UpdateSubscriptionResponse response = new UpdateSubscriptionResponse();
            response.setParameters(actualParameters);
            if (!failedParameters.isEmpty()) {
                response.setError("Could not set parameters " + String.join(", ", failedParameters));
            }

            ClientConnectionUtils.convertAndReply(connection, request, response);
        } else {
            ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage("Subscription not found"));
        }
    }

    private void onPullSubscriptionRequest(final ClientConnection connection, final PullSubscriptionRequest request) {
        final AbstractTopicProducer topic = subscriptionsRegistry.getTopicProducer(request.getSubscriptionId());
        if (topic != null) {
            if (topic instanceof ScheduledTopicProducer) {
                ((ScheduledTopicProducer) topic).run();
            } else {
                ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage("Subscription type is not scheduled"));
            }
        } else {
            ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage("Subscription not found"));
        }
    }

    @Override
    public boolean supports(final AbstractMessage message) {
        return (message instanceof SubscribeRequest) ||
               (message instanceof UnsubscribeRequest) ||
               (message instanceof UpdateSubscriptionRequest) ||
               (message instanceof PullSubscriptionRequest);
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
