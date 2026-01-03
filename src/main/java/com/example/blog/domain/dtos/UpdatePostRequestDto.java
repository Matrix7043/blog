package com.example.blog.domain.dtos;

import com.example.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequestDto {

    @NotNull(message = "Post is ID is required")
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title must be {min} and {max} char")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 50000, message = "{min} and {max} char")
    private String content;

    @NotNull(message = "Category ID id required")
    private UUID categoryId;

    @Builder.Default
    @Size(max = 10, message = "Maximum {max} tags")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "Status is required")
    private PostStatus status;

}
