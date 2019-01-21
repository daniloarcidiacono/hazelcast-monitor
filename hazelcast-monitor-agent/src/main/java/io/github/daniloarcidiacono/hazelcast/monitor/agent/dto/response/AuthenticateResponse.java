package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField;

@TypescriptDTO
public class AuthenticateResponse extends AbstractMessage {
    public static final String MESSAGE_TYPE = "authenticate_response";
    private String groupName;

    @TypescriptField(required = false)
    private String error;

    public AuthenticateResponse(final String groupName) {
        super(MESSAGE_TYPE);
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
