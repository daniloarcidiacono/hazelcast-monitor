package it.xdnl.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

@TypescriptDTO
public class CompileFiltersResponse extends AbstractMessage {
    public static final String MESSAGE_TYPE = "compile_filters_response";
    private boolean ok;

    @TypescriptField(required = false)
    private String errors;

    public CompileFiltersResponse() {
        super(MESSAGE_TYPE);
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
