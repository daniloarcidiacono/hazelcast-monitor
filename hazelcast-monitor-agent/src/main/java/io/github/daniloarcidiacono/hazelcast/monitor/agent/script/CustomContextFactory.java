package io.github.daniloarcidiacono.hazelcast.monitor.agent.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

/**
 * Replaces the default context factory in order to grant access to non-public class members in scripts.
 */
public class CustomContextFactory extends ContextFactory {
    /**
     * We enable {@value Context#FEATURE_ENHANCED_JAVA_ACCESS} in order to pick all members in non-public classes.
     * @param cx
     * @param featureIndex
     * @return
     */
    @Override
    public boolean hasFeature(final Context cx, final int featureIndex) {
        switch (featureIndex) {
            case Context.FEATURE_DYNAMIC_SCOPE:
                return true;

            // See JavaMembers.constructor!
            case Context.FEATURE_ENHANCED_JAVA_ACCESS:
                return true;

        }

        return super.hasFeature(cx, featureIndex);
    }
}
