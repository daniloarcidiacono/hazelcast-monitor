package it.xdnl.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.topic.AbstractTopic;

@TypescriptDTO
public class CompileFiltersRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "compile_filters";
    private String source;

    public CompileFiltersRequest() {
        super(MESSAGE_TYPE);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
