package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class UserDto {
    @Null
    private Long id;
    @NotBlank
    private String name;
    @NotBlank(message = "Email must be not empty")
    @Email(message = "Email must be correct")
    private String email;
}
