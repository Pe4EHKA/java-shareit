package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemUpdateDto {
    @NotNull
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
