package com.hadef.movieslist.service.impl;

import com.hadef.movieslist.domain.dto.ChangePassword;
import com.hadef.movieslist.domain.dto.MailBody;
import com.hadef.movieslist.domain.entity.ForgotPassword;
import com.hadef.movieslist.domain.entity.User;
import com.hadef.movieslist.repository.ForgotPasswordRepository;
import com.hadef.movieslist.repository.UserRepository;
import com.hadef.movieslist.service.EmailService;
import com.hadef.movieslist.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String verifyEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found"));
        int otp = generateOtp();
        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .text("This is the otp: "+otp)
                .subject("OTP verification")
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expirationDate(new Date(System.currentTimeMillis()+70*1000))
                .user(user)
                .build();
        emailService.sendSimpleEmail(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return "Email is sent for verification";
    }

    @Override
    public boolean verifyOtp(Integer otp, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found")
        );
        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found")
        );
        if(forgotPassword.getExpirationDate().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            return false;
        }
        forgotPassword.setVerified(true);
        forgotPasswordRepository.save(forgotPassword);
        return true;
    }

    @Override
    public boolean resetPassword(ChangePassword changePassword, String email) {
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            return false;
        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email,encodedPassword);
        return true;
    }

    @Override
    public boolean isEmailVerified(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found")
        );
        ForgotPassword forgotPassword = forgotPasswordRepository.findByUser(user).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found in forget password")
        );
        return forgotPassword.isVerified();
    }


    private Integer generateOtp() {
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
