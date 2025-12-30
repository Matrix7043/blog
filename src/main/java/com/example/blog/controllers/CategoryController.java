package com.example.blog.controllers;

import com.example.blog.domain.dtos.CategoryDto;
import com.example.blog.domain.dtos.CreateCategoryRequestDto;
import com.example.blog.domain.entities.Category;
import com.example.blog.mappers.CategoryMapper;
import com.example.blog.repositories.CategoryRepository;
import com.example.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    final CategoryService categoryService;
    final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategory(){
       List<CategoryDto> categories = categoryService.listCategories()
               .stream().map(categoryMapper::toDto)
               .toList();
       return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto){
        Category category = categoryMapper.toEntity(createCategoryRequestDto);
        Category savedCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory), HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
