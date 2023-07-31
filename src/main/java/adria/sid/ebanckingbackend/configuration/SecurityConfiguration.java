package adria.sid.ebanckingbackend.configuration;

import adria.sid.ebanckingbackend.ennumerations.PERMISSION;
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
                "/api/v1/compte/**",
                "/api/v1/auth/**",
                "/api/v1/register/**",
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
        .requestMatchers(GET,"/api/v1/notification/**").hasAnyRole(BANQUIER.name(),CLIENT.name())

        .requestMatchers(GET, "/api/v1/client/comptes").hasRole(CLIENT.name())
        .requestMatchers(POST, "/api/v1/compte/demande_suspend/**").hasRole(CLIENT.name())
        .requestMatchers(POST, "/api/v1/compte/demande_activer/**").hasRole(CLIENT.name())
        .requestMatchers(POST, "/api/v1/compte/demande_block/**").hasRole(CLIENT.name())

        .requestMatchers(POST, "/api/v1/compte/**").hasAuthority(PERMISSION.ACTIVER_ACCOUNT.name())
        .requestMatchers(GET, "/api/v1/compte/**").hasRole(BANQUIER.name())
        .requestMatchers(POST, "/api/v1/compte/blocker/**").hasRole(BANQUIER.name())
        .requestMatchers(POST, "/api/v1/compte/activer/**").hasRole(BANQUIER.name())
        .requestMatchers(POST, "/api/v1/compte/suspender/**").hasRole(BANQUIER.name())
        .requestMatchers(POST, "/api/v1/compte/change_solde/**").hasRole(BANQUIER.name())
        .requestMatchers(GET, "/api/v1/client/comptes/**").hasRole(BANQUIER.name())

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
