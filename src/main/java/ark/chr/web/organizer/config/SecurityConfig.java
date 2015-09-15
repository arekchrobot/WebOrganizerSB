package ark.chr.web.organizer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Arek on 2015-06-27.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int BCRYPT_PASSWORD_STRENGTH = 11;

    @Autowired
    private UserDetailsService userDetailsServiceAdapter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/secured/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login.xhtml").loginProcessingUrl("/j_spring_security_check").permitAll()
                .defaultSuccessUrl("/secured/home.xhtml", true)
                .failureUrl("/loginError.xhtml")
                .and()
                .logout().logoutUrl("/j_spring_security_logout").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(createDaoAuthenticationProvider());
    }

    private DaoAuthenticationProvider createDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceAdapter);
        daoAuthenticationProvider.setPasswordEncoder(createBCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder createBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_PASSWORD_STRENGTH);
    }

}
