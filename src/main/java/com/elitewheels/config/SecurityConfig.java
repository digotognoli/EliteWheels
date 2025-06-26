package com.elitewheels.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toStaticResources; // IMPORTANTE

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/**")
            )
            .authorizeHttpRequests(auth -> auth
                // MÉTODO CORRIGIDO E MAIS MODERNO:
                // Libera o acesso a todas as pastas estáticas comuns (css, js, images).
                .requestMatchers(toStaticResources().atCommonLocations()).permitAll()
                
                // Libera o acesso às páginas públicas.
                .requestMatchers("/login", "/cadastro", "/cadastrar", "/error").permitAll()
                
                // Exige autenticação para qualquer outra requisição.
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/orcar", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}