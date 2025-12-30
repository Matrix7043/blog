package com.example.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCategoryRequestDto {

    @NotBlank(message = "Category must not be blank")
    @Size(min = 2, max = 50, message = "Category must be between {min} and {max} chars")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Category cannot have special characters, only (-),( )")
    private String name;

    
}
