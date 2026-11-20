package me.jaehun.springdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleUpdateRequest {
    private String title;
    private String content;

//    public ArticleUpdateRequest(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
}
