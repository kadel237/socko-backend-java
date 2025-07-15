package com.soko_backend.service;

public class MockMailService implements MailService{
    @Override
    public void sendResetCode(String to , String code){
        System.out.println("code de verification envoyé à " + to + ":" + code);
    }
}
