package com.hadef.movieslist.repository;

import com.hadef.movieslist.domain.entity.ForgotPassword;
import com.hadef.movieslist.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, UUID> {

    @Query("SELECT C FROM ForgotPassword C WHERE C.otp= ?1 and C.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    Optional<ForgotPassword> findByUser(User user);
}
