package com.pvictorcr.bolaodacopa;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.pvictorcr.bolaodacopa.security.CustomOAuth2UserService;
import com.pvictorcr.bolaodacopa.services.UserService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomOAuth2UserService oauthUserService;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.requiresChannel()
        .anyRequest()
        .requiresSecure()
        .and()
		.authorizeRequests()
		.antMatchers("/", "/bolaodacopa/**", "/oauth_login", "/error", "/webjars/**", "/h2-console/**")
		.permitAll()
		//.antMatchers("/patient/**").access("hasRole('" + UserRole.ROLE_PATIENT.name() + "')")
		//.antMatchers("/professional/**").access("hasRole('" + UserRole.ROLE_PROFESSIONAL.name() + "')")
		.anyRequest().authenticated()
		.and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
		.and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
		.and()
		.oauth2Login()
		.loginPage("/oauth_login")
		.defaultSuccessUrl("/bolaodacopa/loginsuccess", true)
		//.loginPage("/login")
	    .userInfoEndpoint()
	        .userService(oauthUserService)
	    .and()
	    .successHandler(new AuthenticationSuccessHandler() {
	 
	        @Override
	        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                Authentication authentication) throws IOException, ServletException {
	            
	            DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
	            String email = oauthUser.getAttribute("email");
	            String nome = oauthUser.getFullName();
	            boolean isNew = userService.processOAuthPostLogin(email, nome);
	 
	            response.sendRedirect("/bolaodacopa/loginsuccess" + (isNew ? "?newuser=true" : ""));
	        }
	    })
		/*.and()
            .formLogin()
                //.loginPage("/login")
                .defaultSuccessUrl("/")
                //.permitAll()*/
		.and()
		.logout()
		.permitAll();
	}

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("password").roles("USER");
    }
}