package com.example.springoot.controllers;


import com.example.springoot.models.Company;
import com.example.springoot.security.CustomUserDetailsService;
import com.example.springoot.services.CompanyService;
import com.example.springoot.services.userService;
import com.example.springoot.services.PosessionService;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.models.Posessions;
import com.example.springoot.models.PosessionsHelper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private userService UserService;
    private CompanyService companyService;

    private PosessionService posessionService;
    public AuthController(userService UserService, CompanyService companyService, PosessionService posessionService){
        this.UserService = UserService;
        this.companyService = companyService;
        this.posessionService = posessionService;
    }
    @GetMapping("/")
    public String home(){
        return "homepage";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        loginCredentials user = new loginCredentials();
        model.addAttribute("user", user);
        return "registerpage";
    }
    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") loginCredentials user,
                                BindingResult result,
                               Model model){
        loginCredentials existingUser = UserService.findUserByUsername(user.getUsername());
        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null,
                    "There is already an account registered with the same username");
        }
        if (result.hasErrors()){
            model.addAttribute("user", user);
            return "redirect:/register?error";
        }
        UserService.saveUser(user);
        return "redirect:/register?success";
    }
    @GetMapping("/login")
    public String login(Model model){
        return "loginpage";
    }
    @GetMapping("/result")
    public String portfolio(Model model, Principal principal){
        List<Company> companies = companyService.findAllCompanies();
        PosessionsHelper newTnx = new PosessionsHelper();
        model.addAttribute("transaction", newTnx);
        String username = "";
        if(principal!= null) username = principal.getName();
        loginCredentials user = UserService.findUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("company_values", companies);
        List<String> change = new ArrayList<String>();
        List<Long> share_in_company = new ArrayList<>();
        for(int i = 0; i< 5; i++){
            change.add(String.format("%.2f",(companies.get(i).getWorth_per_share()-companies.get(i).getOld_worth_per_share())/companies.get(i).getOld_worth_per_share()*100));
            Posessions user_at_company = posessionService.findPreviousTransaction(companies.get(i), user);
            if(user_at_company == null){
                share_in_company.add(0L);
            }
            else{
                share_in_company.add(user_at_company.getAmount());
            }
        }
        model.addAttribute("change", change);
        model.addAttribute("share_in_company", share_in_company);
        return "result";
    }
    @PostMapping("/result/transaction")
    public String transaction(@ModelAttribute("transaction") PosessionsHelper newTnx, Principal principal){


        Company company = companyService.findCompanyByCompanyname(newTnx.getCompany());
        loginCredentials user = UserService.findUserByUsername(principal.getName());
        Posessions existingTransaction = posessionService.findPreviousTransaction(company,user);
        if(user.getCredit() < company.getWorth_per_share() * newTnx.getAmount()) {
            return "redirect:/result?error";
        }
        if(existingTransaction == null){
            Posessions transaction = new Posessions();
            transaction.setAmount(newTnx.getAmount());
            transaction.setCost( company.getWorth_per_share() * newTnx.getAmount());
            transaction.setUser(user);
            transaction.setCompany(company);
            posessionService.savePosession(transaction);
        }
        else{
            if(newTnx.getAmount() + existingTransaction.getAmount() < 0 || newTnx.getAmount() + existingTransaction.getAmount() > company.getFree_share()){
                return "redirect:/result?error";
            }
            existingTransaction.setAmount(newTnx.getAmount() + existingTransaction.getAmount());
            existingTransaction.setCost(existingTransaction.getCost() + newTnx.getAmount() * company.getWorth_per_share());
            posessionService.savePosession(existingTransaction);
        }
        user.setCredit(user.getCredit() - company.getWorth_per_share()* newTnx.getAmount());
        UserService.saveUser(user);
        company.setFree_share(company.getFree_share()- newTnx.getAmount());
        companyService.saveCompany(company);

        return "redirect:/result";
    }
}
