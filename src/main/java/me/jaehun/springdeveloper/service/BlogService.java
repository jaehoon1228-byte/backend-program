package me.jaehun.springdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.jaehun.springdeveloper.dao.Article;
import me.jaehun.springdeveloper.dto.ArticleCreationRequest;
import me.jaehun.springdeveloper.repository.BlogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(ArticleCreationRequest articleCreationRequest) {
        return blogRepository.save(articleCreationRequest.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article getById(Long id) {
        return blogRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }
}
