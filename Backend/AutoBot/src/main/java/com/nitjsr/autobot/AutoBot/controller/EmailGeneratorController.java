package com.nitjsr.autobot.AutoBot.controller;

import com.nitjsr.autobot.AutoBot.payload.EmailRequest;
import com.nitjsr.autobot.AutoBot.service.EmailGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/email")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneratorController {

    @Autowired
    private final EmailGeneratorService emailGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmailResponse(@RequestBody EmailRequest emailRequest) {
        String response = emailGeneratorService.emailGenerator(emailRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
