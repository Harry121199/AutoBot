package com.nitjsr.autobot.AutoBot.payload;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String subjectContent;
    private String tone;
}
