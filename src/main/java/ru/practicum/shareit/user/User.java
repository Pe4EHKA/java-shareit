package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank(message = "Email must be not empty")
    @Email(message = "Email must be correct")
    private String email;
}
