package com.qinxi1992.im.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/webjars/**", "/register", "/api/common/**", "/image/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()//.loginPage("/login")
                .defaultSuccessUrl("/index", true).permitAll()
                .and().logout().permitAll()
                .and().csrf().disable();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("zhangsan").password("123456").roles("USER")
                .and().withUser("lisi").password("123456").roles("USER");
    }

}
