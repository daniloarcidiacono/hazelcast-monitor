package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.ErrorMessage;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.request.*;
import it.xdnl.hazelcast.monitor.agent.dto.response.SubscribeResponse;
import it.xdnl.hazelcast.monitor.agent.dto.response.UpdateSubscriptionResponse;
import it.xdnl.hazelcast.monitor.agent.exception.UpdateParameterException;
import it.xdnl.hazelcast.monitor.agent.factory.TopicProducerFactory;
import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.helper.FilterRegistry;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;
import it.xdnl.hazelcast.monitor.agent.producer.AbstractTopicProducer;
import org.codehaus.commons.compiler.CompileException;

public class SubscribeMessageHandler implements MessageHandler {
    private ConnectionSubscriptionsRegistry registry = new ConnectionSubscriptionsRegistry();
    private TopicProducerFactory topicProducerFactory;

    public SubscribeMessageHandler(final TopicProducerFactory topicProducerFactory) {
        this.topicProducerFactory = topicProducerFactory;
        this.topicProducerFactory.setConnectionSubscriptionsRegistry(registry);
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

        if (genericMessage instanceof CompileFiltersRequest) {
            onCompileFiltersRequest(connection, (CompileFiltersRequest)genericMessage);
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

    private void onUpdateSubscription(final ClientConnection connection, final UpdateSubscriptionRequest request) {
        final AbstractTopicProducer topic = registry.getTopicProducer(request.getSubscriptionId());
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

    private void onCompileFiltersRequest(final ClientConnection connection, final CompileFiltersRequest request) {
        final FilterRegistry filterRegistry = topicProducerFactory.getFilterRegistry();
        try {
            filterRegistry.compileFilters(request.getSource());

            // Success
            final CompileFiltersResponse response = new CompileFiltersResponse();
            response.setOk(true);
            ClientConnectionUtils.convertAndReply(connection, request, response);
        } catch (CompileException e) {
            // Compile error
            final CompileFiltersResponse response = new CompileFiltersResponse();
            response.setOk(false);
            response.setErrors(e.getMessage());

            ClientConnectionUtils.convertAndReply(connection, request, response);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            // Other internal error
            ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage(e.getMessage()));
        }
    }

    @Override
    public boolean supports(final AbstractMessage message) {
        return (message instanceof SubscribeRequest) ||
               (message instanceof UnsubscribeRequest) ||
               (message instanceof UpdateSubscriptionRequest) ||
               (message instanceof CompileFiltersRequest);
    }
}
