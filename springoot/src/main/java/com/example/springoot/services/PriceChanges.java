package com.example.springoot.services;

import com.example.springoot.models.Company;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
@Component
public class PriceChanges {


    private CompanyService companyService;
    public PriceChanges(CompanyService companyService){
        this.companyService = companyService;
    }
    @Scheduled(fixedDelay = 30000)
    public void updatePrices(){
        List<Company> companies = companyService.findAllCompanies();
        Random rand = new Random();

        for(int i = 0; i < 5; i++){
            float ch = rand.nextFloat();
            float mlt = rand.nextFloat();
            companies.get(i).setOld_worth_per_share(companies.get(i).getWorth_per_share());
            float newWorth_per_share = companies.get(i).getWorth_per_share() + (ch - mlt) * Math.max(companies.get(i).getWorth_per_share(), 1);
            if(newWorth_per_share < 0){
                newWorth_per_share = -1 * newWorth_per_share;
            }
            companies.get(i).setWorth_per_share(newWorth_per_share);
            companyService.saveCompany(companies.get(i));
        }

    }
}
