package org.molsh.security;

import org.molsh.common.UserRole;
import org.molsh.exception.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/user", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/task/test", "/task/process", "/task/add/*").hasRole(UserRole.Admin.name())
                        .anyRequest().authenticated())
                .formLogin(login -> {
                    Customizer.withDefaults().customize(login);
                    login.defaultSuccessUrl("/swagger-ui/index.html", true)
                            .permitAll();
                })
                .logout(Customizer.withDefaults())
                .exceptionHandling(exceptions -> {
                    Customizer.withDefaults().customize(exceptions);
                    exceptions.accessDeniedHandler(new CustomAccessDeniedHandler());
                });
        return http.build();
    }
}
