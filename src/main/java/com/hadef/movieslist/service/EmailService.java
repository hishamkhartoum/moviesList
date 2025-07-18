package com.hadef.movieslist.service;

import com.hadef.movieslist.domain.dto.MailBody;

public interface EmailService {
    void sendSimpleEmail(MailBody mail);
}
