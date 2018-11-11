package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TypescriptDTO
public class ErrorMessage extends AbstractMessage {
    public static final String MESSAGE_TYPE = "error";
    private List<String> errors = new ArrayList<>();

    public ErrorMessage() {
        super(MESSAGE_TYPE);
    }

    public ErrorMessage(final String ...errors) {
        super(MESSAGE_TYPE);
        this.errors.addAll(Arrays.asList(errors));
    }

    public ErrorMessage(final Iterable<String> errors) {
        super(MESSAGE_TYPE);
        for (String error : errors) {
            this.errors.add(error);
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
