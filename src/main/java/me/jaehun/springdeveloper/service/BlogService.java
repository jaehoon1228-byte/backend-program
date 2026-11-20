package me.jaehun.springdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.jaehun.springdeveloper.dao.Article;
import me.jaehun.springdeveloper.dto.ArticleCreationRequest;
import me.jaehun.springdeveloper.dto.ArticleUpdateRequest;
import me.jaehun.springdeveloper.repository.BlogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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


//    public Article updateArticle(Long id, ArticleUpdateRequest request) {
//        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Article not found"));
//        article.update(request.getTitle(), request.getContent());
//        return blogRepository.save(article);
//    }

    @Transactional
    public Article updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Article not found"));
        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
