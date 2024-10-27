package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateDto {
    @NotNull
    private Long id;
    private String name;
    private String email;
}
