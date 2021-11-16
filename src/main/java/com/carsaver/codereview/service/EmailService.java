package com.carsaver.codereview.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendConfirmation(String email) {
        System.out.print("sending confirmation to " + email);
    }

}
