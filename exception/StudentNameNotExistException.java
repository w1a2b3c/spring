package com.example.system.exception;

public class StudentNameNotExistException extends BaseException{
    public StudentNameNotExistException(){};
    public StudentNameNotExistException(String msg){
        super(msg);
    }
}
