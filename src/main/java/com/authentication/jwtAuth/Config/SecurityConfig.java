package com.authentication.jwtAuth.Config;

import com.authentication.jwtAuth.Security.CustomAccessDeniedHandler;
import com.authentication.jwtAuth.Security.JwtAuthenticationEntryPoint;
import com.authentication.jwtAuth.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;


    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // Cross Site Request Forgery.
        http.csrf(csrf-> csrf.disable())
                .cors(cors-> cors.disable())
                .authorizeHttpRequests(
                        auth->auth
                                .requestMatchers("home/admin/**").hasRole("ADMIN")
                                .requestMatchers("home/**").authenticated()
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/create-user").permitAll()
                                .requestMatchers("/admin/create-project").hasRole("ADMIN")
                                .requestMatchers("/admin/delete-project/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .exceptionHandling(ex->{ex.authenticationEntryPoint(point);ex.accessDeniedHandler(accessDeniedHandler);})
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider dodaoAuthenticationProvider(){

        DaoAuthenticationProvider Provider = new DaoAuthenticationProvider();
        Provider.setUserDetailsService(userDetailService);
        Provider .setPasswordEncoder(passwordEncoder);
        return Provider;

    }

}
