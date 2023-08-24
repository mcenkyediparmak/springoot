package com.example.springoot.services.impl;

import com.example.springoot.models.Company;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.repositories.CompanyRepository;
import com.example.springoot.services.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository compRepo){
        this.companyRepository = compRepo;
    }

    @Override
    public List<Company> findAllCompanies(){
        return companyRepository.findAll();
    }

    @Override
    public Company findCompanyByCompanyname(String name){ return companyRepository.findByName(name);}

    @Override
    public void saveCompany(Company company){
        Company ctosave = companyRepository.findByName(company.getName());
        ctosave.setFree_share(company.getFree_share());
        ctosave.setWorth_per_share(company.getWorth_per_share());
        ctosave.setOld_worth_per_share(company.getOld_worth_per_share());
        companyRepository.save(ctosave);
    }

}
