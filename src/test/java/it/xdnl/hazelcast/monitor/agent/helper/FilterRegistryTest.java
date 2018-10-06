package it.xdnl.hazelcast.monitor.agent.helper;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FilterRegistryTest {
    private static final String SOURCE =
        "import it.xdnl.hazelcast.monitor.agent.filter.ICollectionFilter;\n" +
        "import it.xdnl.hazelcast.monitor.agent.filter.IMapFilter;\n" +
        "\n" +
        "public class PassFilter implements ICollectionFilter, IMapFilter {\n" +
        "  @Override\n" +
        "  public boolean matches(final Object element) {\n" +
        "    return true;\n" +
        "  }\n" +
        "  \n" +
        "  @Override\n" +
        "  public boolean matches(final Object key, final Object value) {\n" +
        "    return true;\n" +
        "  }\n" +
        "} \n" +
        "\n" +
        "public class NoneFilter implements ICollectionFilter, IMapFilter {\n" +
        "  @Override\n" +
        "  public boolean matches(final Object element) {\n" +
        "    return false;\n" +
        "  }\n" +
        "  \n" +
        "  @Override\n" +
        "  public boolean matches(final Object key, final Object value) {\n" +
        "    return false;\n" +
        "  }\n" +
        "}\n";

    @Test
    public void multipleClasses() throws Exception {
        final FilterRegistry registry = new FilterRegistry();
        registry.compileFilters(SOURCE);

        assertNotNull(registry.getFilterByName("PassFilter"));
        assertNotNull(registry.getFilterByName("NoneFilter"));
    }
}