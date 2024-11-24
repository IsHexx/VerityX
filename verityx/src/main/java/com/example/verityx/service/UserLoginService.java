package com.example.verityx.service;

import com.example.verityx.entity.UserLogin;

public interface UserLoginService {
    UserLogin login(String username, String password);
}
