package ark.chr.web.organizer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by Arek on 2015-06-28.
 */
@Configuration
@PropertySource("classpath:mail.properties")
public class JavaMailConfig {

    @Value("${mail.transporty.protocol}")
    private String protocol;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private int port;

    @Value("${mail.user}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private boolean auth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${message.source.basename}")
    private String basename;

    @Bean
    public JavaMailSender createJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", auth);
        mailProps.put("mail.smtp.starttls.enable", starttls);

        javaMailSender.setJavaMailProperties(mailProps);
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        return javaMailSender;
    }

    @Bean
    public ResourceBundleMessageSource createMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);

        return messageSource;
    }
}
