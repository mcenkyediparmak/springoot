package com.example.springoot.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springoot.models.Company;
public interface CompanyRepository extends JpaRepository<Company, Long>{

    Company findByName(String name);
}
