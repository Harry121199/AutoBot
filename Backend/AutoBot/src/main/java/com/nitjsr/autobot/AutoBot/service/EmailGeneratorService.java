package com.nitjsr.autobot.AutoBot.service;


import com.nitjsr.autobot.AutoBot.payload.EmailRequest;

public interface EmailGeneratorService {
    public String emailGenerator(EmailRequest emailRequest);

}

