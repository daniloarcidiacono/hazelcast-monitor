package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

/**
 * Request object sent by the clients to check if they can access to
 * a specific Hazelcast group.
 */
@TypescriptDTO
public class CheckAuthenticationRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "check_authentication";

    /**
     * Group name for checking the authentication status.
     */
    private String groupName;

    public CheckAuthenticationRequest() {
        super(MESSAGE_TYPE);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
