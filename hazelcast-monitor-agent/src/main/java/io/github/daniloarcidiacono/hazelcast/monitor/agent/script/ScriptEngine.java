package io.github.daniloarcidiacono.hazelcast.monitor.agent.script;

import org.mozilla.javascript.*;

import java.util.Map;

/**
 * The main script engine class, based on Rhino.
 * All methods are thread-safe.
 */
public class ScriptEngine {
    private static final ScriptEngine INSTANCE = new ScriptEngine();

    public static ScriptEngine getInstance() {
        return INSTANCE;
    }

    private ScriptEngine() {
        // Initialize GlobalFactory with custom factory
        ContextFactory.initGlobal(new CustomContextFactory());

        init();
    }

    /**
     * Represents a compiled script ready to run.
     * Hides the internal Rhino objects.
     */
    public static class CompiledScript {
        private final String source;
        private final ScriptableObject baseScope;
        private final Script script;

        public CompiledScript(final String source, final ScriptableObject baseScope) {
            this.source = source;
            this.baseScope = baseScope;

            final Context ctx = Context.enter();
            try {
                ctx.setLanguageVersion(Context.VERSION_ES6);

                // Compiles the script to Java bytecode, rather than interpreting it.
                // Other features like static analysis are performed.
                ctx.setOptimizationLevel(9);

                // Compile the script
                script = ctx.compileString(source, "script", 1, null);
            } finally {
                Context.exit();
            }
        }


        /**
         * Executes the script, passing the given variables to the scope.
         * This method is thread safe.
         *
         * @param scopeVariables
         * @param <T> the expected result type
         * @return the script result
         */
        public <T> T exec(final Map<String, Object> scopeVariables) {
            // The Rhino Context object is used to store thread-specific information about the execution environment.
            // There should be one and only one Context associated with each thread that will be executing JavaScript.
            final Context ctx = Context.enter();

            try {
                // So to share information across multiple scopes, we first create the object we wish to share.
                // Typically this object will have been created with initStandardObjects and may also have additional objects specific to
                // the embedding. Then all we need to do is create a new object and call its setPrototype method to set the prototype to
                // the shared object, and the parent of the new scope to null:
                final Scriptable scope = ctx.newObject(baseScope);
                scope.setPrototype(baseScope);
                scope.setParentScope(null);

                for (Map.Entry<String, Object> variableEntries : scopeVariables.entrySet()) {
                    final String name = variableEntries.getKey();
                    final Object object = variableEntries.getValue();
                    final Object jsProxyObject = Context.javaToJS(object, scope);
                    ScriptableObject.putConstProperty(scope, name, jsProxyObject);
                }

                // We can now use newScope as a scope for calls to evaluate scripts. Let's call this scope the instance scope.
                // Any top-level functions or variables defined in the script will end up as properties of the instance scope.
                // Uses of standard objects like Function, String, or RegExp will find the definitions in the shared scope.
                // Multiple instance scopes can be defined and have their own variables for scripts yet share the definitions in the
                // shared scope. These multiple instance scopes can be used concurrently.
                return (T) script.exec(ctx, scope);
            } finally {
                Context.exit();
            }
        }

        public String getSource() {
            return source;
        }
    }

    // The base scope, parent of all script scopes
    private ScriptableObject baseScope;

    /**
     * Initializes the shared base scope.
     */
    private void init() {
        // The Rhino Context object is used to store thread-specific information about the execution environment.
        // There should be one and only one Context associated with each thread that will be executing JavaScript.
        final Context ctx = Context.enter();
        try {
            // The easiest way to embed Rhino is just to create a new scope this way whenever you need one.
            // However, initStandardObjects is an expensive method to call and it allocates a fair amount of memory.
            // It's important to understand that a scope is independent of the Context that created it.
            // You can create a scope using one Context and then evaluate a script using that scope and another Context.
            baseScope = ctx.initSafeStandardObjects(null, true);
            baseScope.sealObject();
        } finally {
            Context.exit();
        }
    }

    public CompiledScript createScript(final String source) {
        return new CompiledScript(source, baseScope);
    }
}
