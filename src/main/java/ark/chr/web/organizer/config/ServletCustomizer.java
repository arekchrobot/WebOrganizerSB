package ark.chr.web.organizer.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.stereotype.Component;

/**
 * Created by Arek on 2015-09-12.
 */
@Component
public class ServletCustomizer implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("woff","application/x-font-woff");
        mappings.add("eot","application/vnd.ms-fontobject");
        mappings.add("ttf","application/octet-stream");
        configurableEmbeddedServletContainer.setMimeMappings(mappings);
    }
}
