package com.example.springoot.exceptions;

public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(String username){
        super("Could not find user" + username);
    }
}
