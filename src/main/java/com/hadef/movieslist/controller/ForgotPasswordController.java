package com.hadef.movieslist.controller;

import com.hadef.movieslist.domain.dto.ChangePassword;
import com.hadef.movieslist.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/verify/{email}")
    public ResponseEntity<String> verifyEmail(@Valid @PathVariable("email") String email) {
        String verifyEmail = forgotPasswordService.verifyEmail(email);
        return ResponseEntity.ok(verifyEmail);
    }

    @PostMapping("/verify/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @Valid @PathVariable("email") String email) {
        boolean verifyOtp = forgotPasswordService.verifyOtp(otp,email);
        if(verifyOtp){
            return ResponseEntity.ok("Email is verified");
        }
        return new ResponseEntity<>("Failed to verify email", HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping(path = "/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable("email") String email) {
        if(!forgotPasswordService.isEmailVerified(email)){
            return new ResponseEntity<>("Email is not verified", HttpStatus.EXPECTATION_FAILED);
        }
        if(forgotPasswordService.resetPassword(changePassword,email)){
            return ResponseEntity.ok("Email has been changed successfully");
        }
        return new ResponseEntity<>("Please enter the password again",HttpStatus.EXPECTATION_FAILED);
    }
}
