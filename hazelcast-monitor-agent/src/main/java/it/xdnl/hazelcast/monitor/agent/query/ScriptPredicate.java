package it.xdnl.hazelcast.monitor.agent.query;

import it.xdnl.hazelcast.monitor.agent.script.ScriptEngine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Predicate that runs a {@link ScriptEngine.CompiledScript}.
 */
public class ScriptPredicate implements Predicate<Object>, Serializable {
    private final String source;

    // We do not want to serialize the script with Hazelcast
    private transient ScriptEngine.CompiledScript script;

    public ScriptPredicate(final String source) {
        this.source = source;
    }

    /**
     * Compiles the script.
     * This is needed because {@link ScriptPredicate} is transferred among Hazelcast instances.
     */
    public void prepare() {
        if (script == null) {
            script = ScriptEngine.getInstance().createScript(source);
        }
    }

    @Override
    public boolean test(final Object element) {
        final Map<String, Object> params = new HashMap<>();
        params.put("element", element);
        try {
            final Object result = script.exec(params);
            if (result instanceof Boolean) {
                return (boolean)result;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSource() {
        return source;
    }
}
