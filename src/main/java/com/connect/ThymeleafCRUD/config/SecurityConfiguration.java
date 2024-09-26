package com.connect.ThymeleafCRUD.config;

import com.connect.ThymeleafCRUD.entity.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailsService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                registry.requestMatchers("/home", "/register/**").permitAll();    //  all users (both ADMIN and USER) to access the /home endpoint without authentication
                registry.requestMatchers("/admin/**").hasRole("ADMIN");         // Users who have admin role can access
                registry.requestMatchers("/user/**").hasRole("USER");           // Users who have user role can access
                registry.anyRequest().authenticated();                      // any other request must be authenticated
        } )
        .formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer
                    .loginPage("/login")
                    .successHandler(new AuthenticationSuccessHandler())
                    .permitAll();
        })
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails normalUser = User.builder()                                     // custom User
//                .username("demo")
//                .password("$2a$12$.5MtQJr6MzdQZSqsdDSOY.bFzrn5oEnQW66B.11htnaGb7bNRx/kO")
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User.builder()                                      // custom Admin
//                .username("admin")
//                .password("$2a$12$s5AQwqWOvL3TEr0pfboWYuIdUupVXQMjg6TkJlSJXaKUvGSR5mEBi")
//                .roles("ADMIN","USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }

    @Bean
    public UserDetailsService userDetailsService(){
        return userDetailService;
    }

    // implementing authentication and encoding password
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
