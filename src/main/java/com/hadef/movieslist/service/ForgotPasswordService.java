package com.hadef.movieslist.service;

import com.hadef.movieslist.domain.dto.ChangePassword;

public interface ForgotPasswordService {
    String verifyEmail(String email);
    boolean verifyOtp(Integer otp,String email);
    boolean resetPassword(ChangePassword changePassword, String email);

    boolean isEmailVerified(String email);
}
