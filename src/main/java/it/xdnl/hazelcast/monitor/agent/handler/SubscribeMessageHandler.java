package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.ErrorMessage;
import it.xdnl.hazelcast.monitor.agent.dto.Message;
import it.xdnl.hazelcast.monitor.agent.dto.request.topic.StatisticsTopicMessage;
import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeMessage;
import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;
import it.xdnl.hazelcast.monitor.topic.StatisticsTopic;
import it.xdnl.hazelcast.monitor.topic.Topic;

// TODO: Creare factory e passarla come parametro al costruttore!
public class SubscribeMessageHandler implements MessageHandler {
    private ConnectionSubscriptionsRegistry registry = new ConnectionSubscriptionsRegistry();

    private Topic instanceTopic(final SubscribeMessage message) {
        if (message.getTopic() instanceof StatisticsTopicMessage) {
            StatisticsTopicMessage statisticsTopicMessage = (StatisticsTopicMessage)message.getTopic();
            return new StatisticsTopic(statisticsTopicMessage.getInstanceName());
        }

        return null;
    }

    @Override
    public void process(final ClientConnection connection, final Message genericMessage) {
        final SubscribeMessage message = (SubscribeMessage)genericMessage;
        final Topic topic = instanceTopic(message);
        if (topic != null) {
            registry.subscribe(connection, topic);
        } else {
            ClientConnectionUtils.convertAndSend(connection, new ErrorMessage("Unrecognized topic"));
        }
    }

    @Override
    public boolean supports(final Message message) {
        return message instanceof SubscribeMessage;
    }
}
