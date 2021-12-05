package com.example.demo.mapper;

import com.example.demo.pojo.Article;

import java.util.List;

public interface ArticleMapper {

    void batchInsert(List<Article> article);

    void insert(Article article);

    Article queryById(String pmid);
}
