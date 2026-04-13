package com.myself.FinCoreBankingApiApplication.service.impl;

import com.myself.FinCoreBankingApiApplication.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailwithAttachment(EmailDetails emailDetails);
}
