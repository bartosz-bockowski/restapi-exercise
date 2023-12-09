package com.example.restapi.security.user;

import com.example.restapi.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private UserService underTest;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserService(userRepository);
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

        underTest.findByUserName(anyString());

        verify(userRepository).findByUsername(anyString());
        assertDoesNotThrow(() -> underTest.findByUserName(anyString()));
        assertNotNull(underTest.findByUserName(anyString()));
    }

    @Test
    void testUserServiceFindById() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(new User()));

        underTest.findById(anyLong());
        verify(userRepository).findById(anyLong());
        assertDoesNotThrow(() -> underTest.findById(anyLong()));
        assertNotNull(underTest.findById(anyLong()));
    }

    @Test
    void testUserServiceGetLoggedUser() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
        given(authentication.getName()).willReturn("fjfx");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertDoesNotThrow(() -> underTest.getLoggedUser());
        assertNotNull(underTest.getLoggedUser());
    }

    @Test
    void testUserServiceUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("123123123");
        user.setName("name");
        user.setSurname("surname");
        user.setAge(1);
        user.setPesel("12312312312");
        assertDoesNotThrow(() -> underTest.updateUser(user));
        User updatedUser = underTest.updateUser(user);
        System.out.println(updatedUser);
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
    }

}