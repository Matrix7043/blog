package com.example.blog.mappers;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.UpdatedPostRequest;
import com.example.blog.domain.dtos.CreatePostRequestDto;
import com.example.blog.domain.dtos.PostDto;
import com.example.blog.domain.dtos.UpdatePostRequestDto;
import com.example.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatedPostRequest toUpdatedPostRequest(UpdatePostRequestDto dto);
}
