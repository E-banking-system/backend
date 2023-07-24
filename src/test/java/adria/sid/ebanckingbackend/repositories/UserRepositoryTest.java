package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        String email = "test@gmail.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        Optional<UserEntity> actualUser = userRepository.findByEmail(email);

        assertEquals(expectedUser, actualUser.orElse(null));
    }
}
