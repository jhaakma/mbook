package mbook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import mbook.page.Page;
import mbook.page.PageService;


@EnableWebSecurity
public class SpringSecurityWebAppConfig  {    

    
	@Configuration

	@Order(1)
	public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**")
			.authorizeRequests()
			    .anyRequest()
			    .authenticated()
			.and().httpBasic()
			.and().sessionManagement()
			    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().csrf()
			    .disable();
			
			 http.headers().addHeaderWriter(
	                    new StaticHeadersWriter("Access-Control-Allow-Origin", "*"));
		}
	}

    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        
        @Autowired
        Environment env;
        @Autowired
        PageService pageService;
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;
        @Autowired 
        UserDetailsService userDetailsService;
        @Autowired
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            
            
            //Authorise each page from entry in DB
            ArrayList<Page> pages = pageService.getPages();
            for ( Page page : pages ) {
                List<String> roles =  page.getAllowedRoles();
                if ( roles == null || roles.isEmpty() ) {
                    http    
                        .authorizeRequests()
                        .antMatchers(page.getHref())
                        .permitAll();
                } else {
                    String[] rolesArray = new String[roles.size()];
                    roles.toArray(rolesArray);
                    http
                        .authorizeRequests()
                        .antMatchers(page.getHref())
                        .hasAnyAuthority( rolesArray );
                } 
            }
            
            http
                .authorizeRequests()
                //Public pages
                    .antMatchers("/", "/favicon.ico", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/login", "/signup", "/confirmRegistration")
                        .permitAll()
                //Everything else: private
                    .anyRequest().authenticated()
                // Login
                .and().formLogin()
                    .successHandler(customAuthenticationSuccessHandler)
                    .loginPage("/login")
                //Logout
                .and().logout()
                    .deleteCookies("JSESSIONID")
                //Remember me token
                .and().rememberMe()
                    .key(env.getProperty("custom.rememberMeKey"))
                    .and().csrf().disable();
        }
    }
}
