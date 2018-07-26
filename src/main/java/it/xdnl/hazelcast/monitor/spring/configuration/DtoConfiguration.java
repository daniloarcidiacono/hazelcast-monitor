package it.xdnl.hazelcast.monitor.spring.configuration;

import it.xdnl.chat.typescript.TypescriptParser;
import it.xdnl.chat.typescript.TypescriptRenderer;
import it.xdnl.chat.typescript.declaration.TypescriptDeclaration;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.product.MembersProduct;
import it.xdnl.hazelcast.monitor.agent.product.Product;
import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "dto.generate", havingValue = "true")
public class DtoConfiguration {
    @PostConstruct
    public void generate() {
        final Reflections reflections = new Reflections("it.xdnl.hazelcast.monitor");
        final TypescriptParser parser = new TypescriptParser(reflections);
        parser.addClass(AbstractMessage.class);
        parser.addClass(Product.class);
        parser.addClass(MembersProduct.MemberSummary.class);
        parser.parse();
        final Map<Class, TypescriptDeclaration> parsedClasses = parser.getParsedClasses();
        final String result = TypescriptRenderer.render(parsedClasses.values());
        System.out.println(result);
    }
}
