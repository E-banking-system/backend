package adria.sid.ebanckingbackend.configuration;

import adria.sid.ebanckingbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static adria.sid.ebanckingbackend.ennumerations.ERole.BANQUIER;
import static adria.sid.ebanckingbackend.ennumerations.ERole.CLIENT;
import static adria.sid.ebanckingbackend.ennumerations.PERMISSION.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers(
                "/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        )
          .permitAll()


        .requestMatchers("/api/v1/client/**").hasRole(CLIENT.name())


        .requestMatchers(GET, "/api/v1/client/**").hasAnyAuthority(CLIENT_READ.name())
        .requestMatchers(POST, "/api/v1/client/**").hasAnyAuthority(CLIENT_CREATE.name())
        .requestMatchers(PUT, "/api/v1/client/**").hasAnyAuthority(CLIENT_UPDATE.name())
        .requestMatchers(DELETE, "/api/v1/client/**").hasAnyAuthority(CLIENT_DELETE.name())

        .requestMatchers("/api/v1/banquier/**").hasRole(BANQUIER.name())

        .requestMatchers(GET, "/api/v1/banquier/**").hasAuthority(BANQUIER_READ.name())
        .requestMatchers(POST, "/api/v1/banquier/**").hasAuthority(BANQUIER_CREATE.name())
        .requestMatchers(PUT, "/api/v1/banquier/**").hasAuthority(BANQUIER_UPDATE.name())
        .requestMatchers(DELETE, "/api/v1/banquier/**").hasAuthority(BANQUIER_DELETE.name())

        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/v1/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    return http.build();
  }
}
