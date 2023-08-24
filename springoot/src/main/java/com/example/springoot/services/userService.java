package com.example.springoot.services;

import com.example.springoot.repositories.credentialsRepository;
import com.example.springoot.models.loginCredentials;

import java.util.List;


public interface userService{
    void saveUser(loginCredentials credentials);

    loginCredentials findUserByUsername(String username);

    List<loginCredentials> findAllUsers();

}