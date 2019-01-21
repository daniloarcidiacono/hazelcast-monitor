package io.github.daniloarcidiacono.hazelcast.monitor.agent;

import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;
import io.github.daniloarcidiacono.typescriptmapper.plugin.DefaultTypescriptMapperMojoConfigurer;
import io.github.daniloarcidiacono.typescriptmapper.plugin.TypescriptMapperMojoParameters;
import org.apache.maven.plugin.logging.Log;

import java.net.URI;
import java.net.URISyntaxException;

public class BolsonArrow extends DefaultTypescriptMapperMojoConfigurer {
    @Override
    public void configure(final TypescriptMapperMojoParameters parameters,
                          final MappingChainBuilder builder,
                          final Log log) throws Exception {
        super.configure(parameters, builder, log);

        getFluentConfigurer()
            .withoutStandardSourceMappers()
            .withSourceMapper((javaClass, declaration) -> {
                try {
                    return new URI("./" + declaration.getIdentifier().charAt(0) + ".ts");
                } catch (URISyntaxException e) {
                    throw new IllegalStateException("Wrong uri", e);
                }
            });
    }
}
