package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

@TypescriptDTO
public class AuthenticateRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "authenticate";

    /**
     * Name of the group to which authenticate.
     */
    private String groupName;

    /**
     * Group password.
     */
    private String groupPassword;

    public AuthenticateRequest() {
        super(MESSAGE_TYPE);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }
}
