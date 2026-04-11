package com.myself.bank_management_system.service.impl;

import com.myself.bank_management_system.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
