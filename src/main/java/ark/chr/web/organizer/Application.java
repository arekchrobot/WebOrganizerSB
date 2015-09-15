package ark.chr.web.organizer;

import com.sun.faces.config.ConfigureListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.faces.webapp.FacesServlet;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories
@ImportResource("classpath:/com/github/javaplugs/jsf/jsfSpringScope.xml")
@EnableAsync
public class Application {

    private static final int LOAD_ON_STARTUP = 1;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public FacesServlet facesServlet() {
        return new FacesServlet();
    }

    @Bean
    public ServletRegistrationBean facesServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                facesServlet(), "*.xhtml");
        registration.setName("FacesServlet");
        registration.setLoadOnStartup(LOAD_ON_STARTUP);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<>(
                new ConfigureListener());
    }
}
