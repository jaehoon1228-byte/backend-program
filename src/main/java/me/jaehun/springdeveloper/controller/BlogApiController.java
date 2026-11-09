package me.jaehun.springdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.jaehun.springdeveloper.dao.Article;
import me.jaehun.springdeveloper.dto.ArticleCreationRequest;
import me.jaehun.springdeveloper.dto.ArticleResponse;
import me.jaehun.springdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;

    // 게시글 추가
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleCreationRequest articleCreationRequest) {
        // 서비스에게 블로그 글 등록을 의뢰.
//        blogService = new BlogService();  // XXXXXXXXXXXXXXXXX
        Article savedArticle = blogService.save(articleCreationRequest);
        // 성공했다면 ResponceEntity 객체를 반환.
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    // 전체 게시글 조회
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        /*
        List<Article> articleList = blogService.findAll();
        List<ArticleResponse> articleResponseList = new ArrayList<>();
        for (Article article : articleList) {
            articleResponseList.add(new ArticleResponse(article));
        }
        */
        List<ArticleResponse> articleResponseList = blogService.findAll().stream().map(ArticleResponse::new).toList();
        return ResponseEntity.ok().body(articleResponseList);
    }

    // 특정 게시글 삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        blogService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 특정 개시글 조회
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id){
        Article article = blogService.getById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

}
