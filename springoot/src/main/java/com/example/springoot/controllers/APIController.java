package com.example.springoot.controllers;


import com.example.springoot.models.Company;
import com.example.springoot.security.CustomUserDetailsService;
import com.example.springoot.services.CompanyService;
import com.example.springoot.services.userService;
import com.example.springoot.services.PosessionService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.models.Posessions;
import com.example.springoot.models.PosessionsHelper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.springoot.assemblers.*;
import java.util.function.*;
import java.util.*;
import java.security.Principal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class APIController {

    private PosessionService posessionService;
    private CompanyService companyService;
    private userService UserService;
    private final PosessionModelAssembler assembler;
    private final Bucket bucket;

    APIController(PosessionService pService, userService uService, CompanyService cService, PosessionModelAssembler assembler){
        this.posessionService = pService;
        this.UserService = uService;
        this.companyService = cService;
        this.assembler = assembler;

        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();

    }

    @GetMapping("/api/investments/{username}")
    public List<Posessions> userPosessions(@PathVariable String username){
        return posessionService.findUserTransactions(UserService.findUserByUsername(username));
    }

    @GetMapping("/api/companies")
    List<Company> companyValues(){
        return companyService.findAllCompanies();
    }

    @GetMapping("/api/users/{username}")
    EntityModel<loginCredentials> getUserInfo(@PathVariable String username){
        loginCredentials user = UserService.findUserByUsername(username);
        return EntityModel.of(user,
                linkTo(methodOn(APIController.class).getUserInfo(username)).withSelfRel(),
                linkTo(methodOn(APIController.class).getAllUsers()).withRel("users"));
    }
    @GetMapping("/api/users")
    CollectionModel<EntityModel<loginCredentials>> getAllUsers(){
        List<EntityModel<loginCredentials>> users = UserService.findAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(APIController.class).getUserInfo(user.getUsername())).withSelfRel(),
                        linkTo(methodOn(APIController.class).getAllUsers()).withRel("users")))
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(APIController.class).getAllUsers()).withSelfRel());
    }


    @PutMapping("/api/maketransaction/{username}")
    ResponseEntity<?> makeInvestment(@RequestBody PosessionsHelper newTnx, @PathVariable String username){
        if(bucket.tryConsume(1)){
            Company company = companyService.findCompanyByCompanyname(newTnx.getCompany());
            loginCredentials user = UserService.findUserByUsername(username);
            Posessions existingTransaction = posessionService.findPreviousTransaction(company, user);
            EntityModel<Posessions> exsEModel = assembler.toModel(existingTransaction);
            if (user.getCredit() < company.getWorth_per_share() * newTnx.getAmount()) {
                return ResponseEntity
                        .created(exsEModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .body(exsEModel);
            }
            if (existingTransaction == null) {
                Posessions transaction = new Posessions();
                transaction.setAmount(newTnx.getAmount());
                transaction.setCost(company.getWorth_per_share() * newTnx.getAmount());
                transaction.setUser(user);
                transaction.setCompany(company);
                posessionService.savePosession(transaction);
                EntityModel<Posessions> tn = assembler.toModel(transaction);
                return ResponseEntity
                        .created(tn.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .body(tn);
            }

            if (newTnx.getAmount() + existingTransaction.getAmount() < 0 || newTnx.getAmount() + existingTransaction.getAmount() > company.getFree_share()) {
                return ResponseEntity
                        .created(exsEModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .body(exsEModel);
            }
            existingTransaction.setAmount(newTnx.getAmount() + existingTransaction.getAmount());
            existingTransaction.setCost(existingTransaction.getCost() + newTnx.getAmount() * company.getWorth_per_share());
            posessionService.savePosession(existingTransaction);
            user.setCredit(user.getCredit() - company.getWorth_per_share() * newTnx.getAmount());
            UserService.saveUser(user);
            company.setFree_share(company.getFree_share() - newTnx.getAmount());
            companyService.saveCompany(company);
            exsEModel = assembler.toModel(existingTransaction);
            return ResponseEntity
                    .created(exsEModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(exsEModel);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

    }


}
