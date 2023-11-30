package com.example.restapi.security.user;

import com.example.restapi.domain.User;

public interface UserService {
    User findByUserName(String name);

    User saveUser(User user);

}