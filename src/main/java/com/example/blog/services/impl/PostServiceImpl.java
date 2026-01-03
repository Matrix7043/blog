package com.example.blog.services.impl;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.PostStatus;
import com.example.blog.domain.UpdatedPostRequest;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.Tag;
import com.example.blog.domain.entities.User;
import com.example.blog.repositories.PostRepository;
import com.example.blog.services.CategoryService;
import com.example.blog.services.PostService;
import com.example.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final int WORDS_PER_MIN = 200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTag(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }
        if(tagId != null){
            Tag tag = tagService.getTag(tagId);
            return  postRepository.findAllByStatusAndTags(
                    PostStatus.PUBLISHED,
                    tag
            );
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagbyIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatedPostRequest updatedPostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        existingPost.setTitle(updatedPostRequest.getTitle());
        existingPost.setContent(updatedPostRequest.getContent());
        existingPost.setReadingTime(calculateReadingTime(updatedPostRequest.getContent()));
        existingPost.setStatus(updatedPostRequest.getStatus());

        UUID updatedPostRequestCategory = updatedPostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatedPostRequest.getCategoryId())) {
           Category category = categoryService.getCategoryById(updatedPostRequestCategory);
           existingPost.setCategory(category);
        }

        Set<UUID> existingTagsIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatedTagIds = updatedPostRequest.getTagIds();
        if(!existingTagsIds.containsAll(updatedTagIds)) {
            List<Tag> NewTagByIds = tagService.getTagbyIds(updatedTagIds);
            existingPost.setTags(new HashSet<>(NewTagByIds));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content) {
        if(content == null || content.isEmpty()) {
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MIN);
    }
}
