package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
