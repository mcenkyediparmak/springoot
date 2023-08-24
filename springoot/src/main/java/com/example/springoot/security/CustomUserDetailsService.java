package com.example.springoot.security;


import com.example.springoot.models.loginCredentials;
import com.example.springoot.repositories.credentialsRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private credentialsRepository userRepo;
    public CustomUserDetailsService(credentialsRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        loginCredentials user = userRepo.findByUsername(username);
        if (user != null){
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), new HashSet<GrantedAuthority>());
        }
        else{
            throw new UsernameNotFoundException("Invalid Username or Password");
        }
    }
}

