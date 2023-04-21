package com.example.security.configuration;

import com.example.security.service.CustomUserDetailService;
import com.example.security.util.CustomPasswordEncoder;
import org.mybatis.spring.annotation.MapperScan;
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


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    private static final String ROLE_VIEW_INFO = "VIEW_INFO";
    private static final String ROLE_VIEW_ADMIN = "VIEW_ADMIN";

    private static final String URL_LOGIN_PAGE = "/login";
    private static final String URL_INFO_ENDPOINT = "/*/info";
    private static final String URL_ABOUT_ENDPOINT = "/*/about";
    private static final String URL_ADMIN_ENDPOINT = "/*/admin";
    private static final String URL_ADD_USER_ENDPOINT = "/*/addUser";
    private static final String URL_GET_BLOCKED_USER_ENDPOINT = "/*/getBlockedUsers";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
		      csrf().disable()
	       .authorizeRequests()									//enable authentication
	       		.antMatchers(URL_ABOUT_ENDPOINT, URL_ADD_USER_ENDPOINT, URL_GET_BLOCKED_USER_ENDPOINT).permitAll()       //for these urls won't ask a password
		      	.antMatchers(URL_INFO_ENDPOINT).hasAnyRole(ROLE_VIEW_INFO)			//for these will ask a password and comparing ROLE
		      	.antMatchers(URL_ADMIN_ENDPOINT).hasAnyRole(ROLE_VIEW_ADMIN)
	       .anyRequest().authenticated()  								//for any other requests will ask me a Login, and only if i fill for with correct value -> it will redirect me to asked by me url
	       .and()
		      .formLogin()
		      .loginPage(URL_LOGIN_PAGE)									//automatically redirect to this page if email ot psw is incorrect, for these urls won't ask a password
		      .usernameParameter("email")  								//for enter with Email instead Login
		      .permitAll()   									// free access for /login request
	       .and()
		      .logout()										// button logout automatically enable and will work
		      .permitAll();
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);

        return mapper;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(customPasswordEncoder);
        provider.setUserDetailsService(customUserDetailService);
        provider.setAuthoritiesMapper(grantedAuthoritiesMapper());

        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }


}
