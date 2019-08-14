package mbook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import mbook.model.Role;


@EnableWebSecurity
@Configuration
public class SpringSecurityWebAppConfig extends WebSecurityConfigurerAdapter {    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomiseAuthenticationSuccessHandler customiseAuthenticationSuccessHandler;

    @Autowired 
    UserDetailsService userDetailsService;
    
    @Autowired
    Environment env;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder);

    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            //Public pages
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/favicon.ico").permitAll()
            //Admin pages
                //.antMatchers("/mbook.api/*").hasAuthority(Role.Type.ADMIN.getValue())
                .antMatchers("/dashboard/**").hasAuthority(Role.Type.ADMIN.getValue())
            //Private pages
                .anyRequest().authenticated()
            
            // Login
            .and().formLogin()
                .successHandler(customiseAuthenticationSuccessHandler)
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
            
            //Logout
            .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
            
            .and().rememberMe()
                .key(env.getProperty("custom.rememberMeKey"))
            
            //API
            .and().csrf().disable();//ignoringAntMatchers("/mbook.api/**");
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
