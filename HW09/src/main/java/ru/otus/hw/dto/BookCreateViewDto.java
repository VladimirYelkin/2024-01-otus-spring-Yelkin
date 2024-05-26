package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateViewDto {

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 128, message = "name min 1. max 128")
    private String title;

    @NotNull(message = "need Author")
    private Long authorId;

    @NotNull(message = "need Genre")
    private Long genreId;
}
