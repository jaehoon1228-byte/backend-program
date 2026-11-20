package me.jaehun.springdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaehun.springdeveloper.dao.Article;
import me.jaehun.springdeveloper.dto.ArticleCreationRequest;
import me.jaehun.springdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlogRepository blogRepository;

    @AfterEach
    public void cleanUp() {
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle : 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // 준비 given : 저장할 데이터를 준비, 요청 URL 준비
        String title = "JUnit 테스트를 통한 블로그 글 생성";
        String content = "수업 종료 10분전, 이 테스트 성공후 점심 식사한다!!!";
        String url = "/api/articles";
        ArticleCreationRequest req = new ArticleCreationRequest(title, content);
        String articleStr = objectMapper.writeValueAsString(req);

        // 실행 when
        ResultActions result =
                mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(articleStr));

        // 검증 then : DB에서 인출해보겠다.
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        Article article = articles.get(0);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
    }

    @DisplayName("getAllArticles : 모든 블로그글 조회에 성공한다.")
    @Test
    public void getAllArticles() throws Exception{
        // 준비 given : url, 데이터
        String url = "/api/articles";
        String title = "11월 5일 두번째 시간";
        String content = "모듈 블로그 조회 테스트 코드 작성";
        blogRepository.save(Article.builder().content(content).title(title).build());

        // 실행 when
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // 검증 then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content));

    }

    @DisplayName("deletArticle : 블로그글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception{
        // 준비 given
        String title = "JUnit 테스트를 통한 블로그글 삭제";
        String url = "/api/articles/{id}";
        String content = "블로그글 삭제 테스트 코드 작성";
        Article savedArticle = blogRepository.save(Article.builder().content(content).title(title).build());

        // 실행 when
        ResultActions resultActions = mockMvc.perform(delete(url, savedArticle.getId()));

        // 검증 then
        resultActions.andExpect(status().isOk());
        assertThat(blogRepository.findById(savedArticle.getId())).isEmpty();
    }

    @DisplayName("findArticle : 블로그글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception{
        // 준비 given
        String title = "JUnit 테스트를 통한 블로그글 조회";
        String content = "블로그글 조회 테스트 코드 작성";
        String url = "/api/articles/{id}";
        Article savedArticle = blogRepository.save(Article.builder().content(content).title(title).build());

        // 실행 when
        ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()).accept(MediaType.APPLICATION_JSON));

        // 검증 then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("updateArticle : 블로그글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception{
        // 준비 given
        String title = "JUnit 테스트를 통한 블로그글 수정";
        String content = "블로그글 수정 테스트 코드 작성";
        String url = "/api/articles/{id}";
        String newTitle = "수정된 블로그글 제목";
        String newContent = "수정된 블로그글 내용";
        ArticleCreationRequest req = new ArticleCreationRequest(newTitle, newContent);
        String articleStr = objectMapper.writeValueAsString(req);
        Article savedArticle = blogRepository.save(Article.builder().content(content).title(title).build());

        // 실행 when
        ResultActions resultActions =
                mockMvc.perform(put(url, savedArticle.getId()).contentType(MediaType.APPLICATION_JSON).content(articleStr));

        // 검증 then
        resultActions.andExpect(status().isOk());
        Article updatedArticle = blogRepository.findById(savedArticle.getId()).get();
        assertThat(updatedArticle.getTitle()).isEqualTo(newTitle);
        assertThat(updatedArticle.getContent()).isEqualTo(newContent);
    }

}