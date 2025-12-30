package com.example.blog.mappers;

import com.example.blog.domain.PostStatus;
import com.example.blog.domain.dtos.CategoryDto;
import com.example.blog.domain.dtos.CreateCategoryRequestDto;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto createCategoryRequestDto);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if(null == posts){
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }

    Category fromDto(CategoryDto categoryDto);

}
