package io.github.daniloarcidiacono.hazelcast.monitor.agent.handler;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.ErrorMessage;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.AuthenticateRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request.CheckAuthenticationRequest;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response.AuthenticateResponse;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response.CheckAuthenticationResponse;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.utils.ClientConnectionUtils;

/**
 * Handler for authentication-related messages.
 *
 * @see AuthenticateRequest
 * @see CheckAuthenticationRequest
 */
public class AuthenticationMessageHandler implements MessageHandler {
    public AuthenticationMessageHandler() {
    }

    @Override
    public void process(final ClientConnection connection, final AbstractMessage genericMessage) {
        if (genericMessage instanceof CheckAuthenticationRequest) {
            onCheckAuthenticationRequest(connection, (CheckAuthenticationRequest)genericMessage);
        }

        if (genericMessage instanceof AuthenticateRequest) {
            onAuthenticateRequest(connection, (AuthenticateRequest)genericMessage);
        }
    }

    private void onAuthenticateRequest(final ClientConnection connection, final AuthenticateRequest request) {
        final HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(request.getGroupName());
        if (instance == null) {
            ClientConnectionUtils.convertAndReply(connection, request, new ErrorMessage("Cluster not found"));
            return;
        }

        final boolean match = instance.getConfig().getGroupConfig().getPassword().equals(request.getGroupPassword());
        final AuthenticateResponse response = new AuthenticateResponse(request.getGroupName());
        if (match || !connection.isAuthenticationRequired()) {
            connection.addGroupAuthentication(request.getGroupName());
        } else {
            response.setError("Invalid password");
        }

        ClientConnectionUtils.convertAndReply(connection, request, response);
    }

    private void onCheckAuthenticationRequest(final ClientConnection connection, final CheckAuthenticationRequest request) {
        ClientConnectionUtils.convertAndReply(connection, request, new CheckAuthenticationResponse(
            request.getGroupName(),
            connection.isAuthenticated(request.getGroupName())
        ));
    }

    @Override
    public boolean supports(final AbstractMessage message) {
        return (message instanceof CheckAuthenticationRequest) ||
               (message instanceof AuthenticateRequest);
    }
}
