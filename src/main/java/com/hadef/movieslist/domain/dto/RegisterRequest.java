package com.hadef.movieslist.domain.dto;

import com.hadef.movieslist.domain.value.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Size(min = 3,max = 50, message = "Email must be between {min} and {max}")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 5,max = 50,message = "Password must be between {min} and {max}")
    private String password;

    private Role role;
}
