
package it.xdnl.hazelcast.monitor.agent.helper;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class SubscriptionRegistryStatistics {
    @TypescriptDTO(identifier = "SubscriptionStatisticsDTO")
    public static class ActiveSubscription {
        private long id;
        private String topic;

        public ActiveSubscription() {
        }

        public ActiveSubscription(long id, String topic) {
            this.id = id;
            this.topic = topic;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    private List<ActiveSubscription> subscriptions = new ArrayList<>();

    public SubscriptionRegistryStatistics() {
    }

    public List<ActiveSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<ActiveSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
