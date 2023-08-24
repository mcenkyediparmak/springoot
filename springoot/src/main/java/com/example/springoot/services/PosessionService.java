package com.example.springoot.services;

import com.example.springoot.repositories.CompanyRepository;
import com.example.springoot.repositories.PosessionRepository;
import com.example.springoot.models.Posessions;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.models.Company;

import java.util.List;
public interface PosessionService {
    void savePosession(Posessions transaction);
    Posessions findPreviousTransaction(Company company, loginCredentials user);

    List<Posessions> findUserTransactions(loginCredentials user);

}
