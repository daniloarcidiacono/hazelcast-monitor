package io.github.daniloarcidiacono.hazelcast.monitor.agent.helper;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class EnvironmentVariable {
    private String name;
    private String value;

    public EnvironmentVariable() {
    }

    public EnvironmentVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
