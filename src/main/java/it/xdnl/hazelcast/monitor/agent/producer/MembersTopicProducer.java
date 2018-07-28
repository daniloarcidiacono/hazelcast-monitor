package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import it.xdnl.hazelcast.monitor.agent.product.MembersProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembersTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "members";
    private static final Logger logger = LoggerFactory.getLogger(MembersTopicProducer.class);
    private HazelcastInstance instance;

    public MembersTopicProducer(final String instanceName) {
        super(TOPIC_TYPE);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public MembersProduct produce() {
        final MembersProduct product = new MembersProduct();
        for (Member member : instance.getCluster().getMembers()) {
            product.getObjects().add(
                new MembersProduct.MemberSummary(
                    member.getAddress().getHost(),
                    member.getAddress().getPort(),
                    member.getVersion().toString(),
                    member.getUuid()
                )
            );
        }

        return product;
    }
}
