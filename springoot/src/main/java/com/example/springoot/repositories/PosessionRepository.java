package com.example.springoot.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springoot.models.Posessions;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.models.Company;
public interface PosessionRepository extends JpaRepository<Posessions, Long> {

    Posessions findByCompanyAndUser(Company company, loginCredentials user);

    List<Posessions> findByUser(loginCredentials user);
}
