package adria.sid.ebanckingbackend.security;

import adria.sid.ebanckingbackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Test
    void testUserDetailsServiceBean() {
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        assertNotNull(userDetailsService);
    }

    @Test
    void testAuthenticationProviderBean() {
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();

        assertNotNull(authenticationProvider);
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        when(config.getAuthenticationManager()).thenReturn(mockAuthenticationManager);

        AuthenticationManager authenticationManager = applicationConfig.authenticationManager(config);

        assertNotNull(authenticationManager);
        verify(config).getAuthenticationManager();
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        assertNotNull(passwordEncoder);
    }
}
