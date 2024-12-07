package com.example.system.exception;

public class AccountCountCantBeZeroException extends BaseException{
    public AccountCountCantBeZeroException(){

    }
    public AccountCountCantBeZeroException(String msg){
        super(msg);
    }
}
