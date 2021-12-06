package com.example.demo.mapper;

import com.example.demo.pojo.Article;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleMapper {

    void batchInsert(List<Article> article);

    void insert(Article article);

    Article queryById(String pmid);

    List<Article> queryPage(@Param("start") int start, @Param("pagesize") int pagesize);

    Integer queryTotal();
}
