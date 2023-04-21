package com.example.secret.configuration;

import com.example.secret.model.Permission;
import com.example.secret.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    public static final String ROLE_STANDART = "STANDART";
    public static final String URL_LOGIN_PAGE = "/login";
    public static final String URL_ABOUT_ENDPOINT = "/*/about";
    public static final String URL_ADD_USER_ENDPOINT = "/*/addUser";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
	       .disable()
	       .authorizeRequests()
		      .antMatchers(URL_LOGIN_PAGE).permitAll()
		      .antMatchers(URL_ADD_USER_ENDPOINT).hasAnyRole(ROLE_STANDART)
		      .antMatchers(URL_ABOUT_ENDPOINT).hasAnyRole(ROLE_STANDART)
	       .anyRequest()
	       .authenticated()
	       .and()
		      .formLogin()
		      .loginPage(URL_LOGIN_PAGE)
		      .usernameParameter("email") 					 //for enter with Email instead Login
	     	      .permitAll()   	        					 // free access for any request
	       .and()
		      .logout()
		      .permitAll();
    }


    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        mapper.setDefaultAuthority(Permission.STANDART.name());

        return mapper;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(customUserDetailService);
        provider.setAuthoritiesMapper(grantedAuthoritiesMapper());

        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

}