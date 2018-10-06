package it.xdnl.hazelcast.monitor.agent.helper;

import it.xdnl.hazelcast.monitor.agent.filter.IFilter;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ByteArrayClassLoader;
import org.codehaus.janino.SimpleCompiler;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterRegistry {
    private Map<String, IFilter> filters = new HashMap<>();

    private final SimpleCompiler sc = new SimpleCompiler();

    public FilterRegistry() {
        sc.setNoPermissions();
    }

    public void compileFilters(final String source) throws CompileException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // Compile
        sc.cook(source);

        // Get the result class loader
        final ByteArrayClassLoader result = (ByteArrayClassLoader)sc.getClassLoader();

        try {
            // Get the loaded classes
            // A bit hackish, we rely on Janino's ByteArrayClassLoader implementation
            final Field classesField = ByteArrayClassLoader.class.getDeclaredField("classes");
            classesField.setAccessible(true);

            // Oh, well
            final Map<String, byte[]> classes = (Map<String, byte[]>) classesField.get(result);

            // For each class
            for (String names : classes.keySet()) {
                // Load it
                final Class<?> arneClass = sc.getClassLoader().loadClass(names);

                // If it's a filter
                if (IFilter.class.isAssignableFrom(arneClass)) {
                    // Instance it and register
                    final IFilter filter = (IFilter)(arneClass.newInstance());
                    registerFilter(names, filter);
                }
            }
        } catch (NoSuchFieldException e) {
            // Yikes, that should never happen (hopefully)
            throw new IllegalStateException("classes field not found in ByteArrayClassLoader!");
        }
    }

    public void registerFilter(final String name, final IFilter filter) {
        filters.put(name, filter);
    }

    public List<String> getFilterNames() {
        final List<String> names = new ArrayList<>();
        names.addAll(filters.keySet());

        return names;
    }

    public IFilter getFilterByName(final String name) {
        return filters.get(name);
    }
}
