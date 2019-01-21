package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField;

import java.util.Map;

@TypescriptDTO
public class CheckAuthenticationResponse extends AbstractMessage {
    public static final String MESSAGE_TYPE = "check_authentication_response";

    private String groupName;
    private boolean authenticated;

    public CheckAuthenticationResponse() {
        super(MESSAGE_TYPE);
    }

    public CheckAuthenticationResponse(final String groupName, final boolean authenticated) {
        super(MESSAGE_TYPE);
        this.groupName = groupName;
        this.authenticated = authenticated;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
