package com.example.springoot.services.impl;

import com.example.springoot.models.loginCredentials;
import com.example.springoot.repositories.credentialsRepository;
import com.example.springoot.services.userService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class userServiceImpl implements userService {
    private credentialsRepository CredentialsRepository;

    public userServiceImpl(credentialsRepository credRepo){
        this.CredentialsRepository = credRepo;
    }

    @Override
    public void saveUser(loginCredentials credentials){
        loginCredentials user = CredentialsRepository.findByUsername(credentials.getUsername());
        if (user == null){
            user = new loginCredentials();
            user.setUsername(credentials.getUsername());
            user.setPassword(credentials.getPassword());
            user.setCredit(credentials.getCredit());

        }
        else{
            user.setCredit(credentials.getCredit());
        }
        CredentialsRepository.save(user);

    }
    @Override
    public loginCredentials findUserByUsername(String username){
        return CredentialsRepository.findByUsername(username);
    }
    @Override
    public List<loginCredentials> findAllUsers(){
        return CredentialsRepository.findAll();
    }

}
