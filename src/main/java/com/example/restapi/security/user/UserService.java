package com.example.restapi.security.user;

public interface UserService {
    User findByUserName(String name);

    User saveUser(User user);

}