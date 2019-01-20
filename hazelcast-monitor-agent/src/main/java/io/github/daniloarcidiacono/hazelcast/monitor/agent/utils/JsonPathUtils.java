package io.github.daniloarcidiacono.hazelcast.monitor.agent.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class JsonPathUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Tries to slice the given object using jsonPath.
     *
     * @param object
     * @param jsonPath may be null
     * @return the sliced object, or null if an error occurs
     */
    public static Object slice(final Object object, final JsonPath jsonPath) {
        if (jsonPath != null) {
            try {
                // Handle collection separately
                if (object instanceof Collection) {
                    final List<Map<String, Object>> json = (List<Map<String, Object>>)((Collection) object).stream().map(x -> mapper.convertValue(x, Map.class)).collect(Collectors.toList());
                    return jsonPath.read(json);
                }

                // Objects
                final Map<String, Object> json = mapper.convertValue(object, Map.class);
                return jsonPath.read(json);
            } catch (Exception e) {
                // Just skip the element:
                //  - entry is a base type so convertValue() fails
                //  - the JsonPath does not match the element (JsonPathException)
                return null;
            }
        }

        return object;
    }

    /**
     * Compiles the given JsonPath string into a {@link JsonPath} object.
     * @param value
     * @return the parsed object, or null if the string is a no-op slicing operation.
     * @throws Exception if the JsonPath string is malformed
     */
    public static JsonPath toJsonPath(final String value) {
        final String trimmed = value.trim();
        if ("$".equals(trimmed) || "$.*".equals(trimmed) || "$..*".equals(trimmed)) {
            return null;
        }

        return JsonPath.compile(trimmed);
    }
}
