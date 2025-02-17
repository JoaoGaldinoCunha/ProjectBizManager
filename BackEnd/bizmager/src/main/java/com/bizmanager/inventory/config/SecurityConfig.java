package com.bizmanager.inventory.config;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${jwt.public.key}")
    private RSAPublicKey rsaPublicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey rsaPrivateKey;


    @Bean
     SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers(HttpMethod.POST, "/company/create").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/company/delete/**").hasAuthority("SCOPE_Company")
                        .requestMatchers(HttpMethod.PUT, "/company/update/**").hasAuthority("SCOPE_Company")
                        .requestMatchers(HttpMethod.GET, "/employees/employeesById/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/employees/allCompanyEmployees/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employees/create").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/employees/delete/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/employees/update/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/stock/allCompanyStocks/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/stock/create").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/stock/delete/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/stock/update/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/product/searchByProductName/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/searchAllProductsByStockId/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/searchProductsMostOrdered/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/searchProductById/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/product/searchProductsWithLowQuantity/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product/create").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/product/delete/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/product/update/quantity/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/product/update/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/order/create").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/order/update/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/order/delete/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/order/allOrders/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/order/odersById/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/login").permitAll()


                        .anyRequest().authenticated())
                .csrf(csrf-> csrf.disable())
                .oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Permitir origem do Angular
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "x-requested-with", "Cache-Control")); // Cabeçalhos permitidos
        configuration.setAllowCredentials(true); // Permitir credenciais
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey
                .Builder(this.rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    @Bean public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
