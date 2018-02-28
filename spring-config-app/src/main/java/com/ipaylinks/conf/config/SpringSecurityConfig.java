package com.ipaylinks.conf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .anyRequest().fullyAuthenticated().and()
                .formLogin().loginPage("/login.htm")
                .failureUrl("/login.htm?error").loginProcessingUrl("/login")
                .permitAll().successForwardUrl("/index.htm").and().logout().permitAll();
    }
}
