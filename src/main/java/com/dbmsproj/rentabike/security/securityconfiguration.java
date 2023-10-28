package com.dbmsproj.rentabike.security;

import com.dbmsproj.rentabike.Service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance;


@Configuration
@EnableWebSecurity
public class securityconfiguration {
    @Autowired
//    @Qualifier()
    private userservice userservices;

    securityconfiguration(userservice userservices) {
        this.userservices = userservices;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userService;
        auth.userDetailsService(userservices).passwordEncoder(getInstance());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
//                                .requestMatchers("/login","/signin","/register","/home","/").permitAll()
                                .requestMatchers("/", "/home", "/register", "login", "logout", "/availableBikes", "/images/*", "/addBike", "/bikes","/homeUser","/profile", "/admin").permitAll()
                                .anyRequest().authenticated()
//                        .requestMatchers("/signin", "/signup","/login","/css/login.css", "/register","/css/*","/js/*","/pics/*").permitAll()
//                        .requestMatchers("/restaurants").hasRole("ADMIN")
//                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/homeUser", true)
                        .permitAll()
                )
                .logout((logout) ->
                        logout.deleteCookies("remove")
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/").permitAll()
                )
                .httpBasic(withDefaults());
        return http.csrf(AbstractHttpConfigurer::disable).build();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}

