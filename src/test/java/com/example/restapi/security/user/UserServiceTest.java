package com.example.restapi.security.user;

import com.example.restapi.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

//@DataJpaTest
//    @MockitoSettings
//    @SpringBootTest
//@MockitoSettings(strictness = Strictness.LENIENT)
//@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
//    @InjectMocks -> istotne
//    @Spy


    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try {
            autoCloseable.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUserServiceFindByUserName() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
        given(authentication.getName()).willReturn("name");

        userService.findByUserName(anyString());

        verify(userRepository).findByUsername(anyString());
        assertDoesNotThrow(() -> userService.findByUserName(anyString()));
        assertNotNull(userService.findByUserName(anyString()));
    }

    @Test
    void testUserServiceFindById() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(new User()));

        userService.findById(anyLong());
        verify(userRepository).findById(anyLong());
        assertDoesNotThrow(() -> userService.findById(anyLong()));
        assertNotNull(userService.findById(anyLong()));
    }

    @Test
    void testUserServiceGetLoggedUser() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
        given(authentication.getName()).willReturn("fjfx");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertDoesNotThrow(() -> userService.getLoggedUser());
        assertNotNull(userService.getLoggedUser());
    }

    //sonarInt
    @Test
    void testUserServiceUpdateUser() {
        User expected = createSampleUser();
        given(userService.updateUser(any(User.class))).willReturn(expected);
//        when(userService.updateUser(any(User.class))).thenReturn(expected);
        User actual = userService.updateUser(expected);
        assertEquals(actual, expected);
        verify(userRepository).save(expected);
//        User user = createSampleUser();
//        assertDoesNotThrow(() -> userService.updateUser(user));
//        User updatedUser = userService.updateUser(user);
//        assertNotNull(updatedUser);
//        assertEquals(user.getId(), updatedUser.getId());
    }

    User createSampleUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("123123123");
        user.setName("name");
        user.setSurname("surname");
        user.setAge(1);
        user.setPesel("12312312312");
        return user;
    }

}