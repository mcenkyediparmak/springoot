package com.example.springoot.services;

import com.example.springoot.models.loginCredentials;
import com.example.springoot.repositories.CompanyRepository;
import com.example.springoot.models.Company;

import java.util.List;
public interface CompanyService {

    List<Company> findAllCompanies();
    Company findCompanyByCompanyname(String name);

    void saveCompany(Company company);
}
