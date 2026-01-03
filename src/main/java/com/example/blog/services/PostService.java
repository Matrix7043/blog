package com.example.blog.services;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.UpdatedPostRequest;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest  createPostRequest);
    Post updatePost(UUID id, UpdatedPostRequest updatedPostRequest);
    Post getPost(UUID id);
    void deletePost(UUID id);
}
