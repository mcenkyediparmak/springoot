package com.example.springoot.services.impl;

import com.example.springoot.models.Posessions;
import com.example.springoot.models.PosessionsHelper;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.models.Company;
import com.example.springoot.repositories.PosessionRepository;

import com.example.springoot.services.PosessionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosessionServiceImpl implements PosessionService {
    private PosessionRepository posessionRepository;
    public PosessionServiceImpl(PosessionRepository posRepo){
        this.posessionRepository = posRepo;
    }

    @Override
    public void savePosession(Posessions transaction){
        Posessions ptosave = posessionRepository.findByCompanyAndUser(transaction.getCompany(),transaction.getUser());
        if(ptosave == null) {
            ptosave = new Posessions();
            ptosave.setCompany(transaction.getCompany());
            ptosave.setUser(transaction.getUser());
            ptosave.setAmount(transaction.getAmount());
            ptosave.setCost(transaction.getCost());
        }
        else{
            ptosave.setCost(ptosave.getCost());
            ptosave.setAmount(ptosave.getAmount());
        }
        posessionRepository.save(ptosave);

    }

    @Override
    public Posessions findPreviousTransaction(Company company, loginCredentials user){
        return posessionRepository.findByCompanyAndUser(company, user);
    }

    @Override
    public List<Posessions> findUserTransactions(loginCredentials user){
        return posessionRepository.findByUser(user);
    }
}
