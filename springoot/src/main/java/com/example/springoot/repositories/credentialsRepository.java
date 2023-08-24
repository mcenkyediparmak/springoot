package com.example.springoot.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springoot.models.loginCredentials;

public interface credentialsRepository extends JpaRepository<loginCredentials, Long>{
    loginCredentials findByUsername(String username);
}