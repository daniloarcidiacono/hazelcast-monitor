package it.xdnl.hazelcast.monitor.agent.helper;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.ClientConnectionListener;
import it.xdnl.hazelcast.monitor.agent.dto.response.TopicNoticeMessage;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;
import it.xdnl.hazelcast.monitor.topic.Topic;
import it.xdnl.hazelcast.monitor.topic.TopicListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConnectionSubscriptionsRegistry implements TopicListener, ClientConnectionListener {
    private Set<Topic> topics = new HashSet<>();
    private Map<ClientConnection, Set<Topic>> connectionTopicsMap = new HashMap<>();
    private Map<Topic, Set<ClientConnection>> topicConnectionsMap = new HashMap<>();

    public synchronized void subscribe(final ClientConnection connection, final Topic topic) {
        topics.add(topic);

        if (!connectionTopicsMap.containsKey(connection)) {
            connectionTopicsMap.put(connection, new HashSet<>());
        }

        if (!topicConnectionsMap.containsKey(topic)) {
            topicConnectionsMap.put(topic, new HashSet<>());
        }

        final Set<Topic> connectionTopics = connectionTopicsMap.get(connection);
        final Set<ClientConnection> topicConnections = topicConnectionsMap.get(topic);
        connectionTopics.add(topic);
        topicConnections.add(connection);

        // Register this class
        topic.addListener(this);
        connection.addListener(this);
    }

    public synchronized void unsubscribe(final ClientConnection connection, final Topic topic) {
        final Set<Topic> connectionTopics = connectionTopicsMap.get(connection);
        final Set<ClientConnection> topicConnections = topicConnectionsMap.get(topic);
        topicConnections.remove(connection);
        connectionTopics.remove(topic);

        if (topicConnections.isEmpty()) {
            topic.removeListener(this);
            topics.remove(topic);
            topicConnectionsMap.remove(topic);
        }

        if (connectionTopics.isEmpty()) {
            connection.removeListener(this);
            connectionTopicsMap.remove(connection);
        }
    }

    public synchronized void unsubscribeAll(final ClientConnection connection) {
        final Set<Topic> connectionTopics = connectionTopicsMap.get(connection);
        for (Topic topic : connectionTopics) {
            final Set<ClientConnection> topicConnections = topicConnectionsMap.get(topic);
            topicConnections.remove(connection);

            if (topicConnections.isEmpty()) {
                topic.removeListener(this);
                topics.remove(topic);
                topicConnectionsMap.remove(topic);
            }
        }

        connection.removeListener(this);
        connectionTopicsMap.remove(connection);
    }

    @Override
    public void notice(final Topic topic, final Object object) {
        final TopicNoticeMessage notice = new TopicNoticeMessage();
        notice.setTopicType(topic.getTopicType());
        notice.setNotice(object);

        for (ClientConnection connection : topicConnectionsMap.get(topic)) {
            ClientConnectionUtils.convertAndSend(connection, notice);
        }
    }

    @Override
    public void closed(final ClientConnection connection) {
        unsubscribeAll(connection);
    }
}
