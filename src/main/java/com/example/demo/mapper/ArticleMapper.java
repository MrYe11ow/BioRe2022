package com.example.demo.mapper;

import com.example.demo.pojo.Article;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 任务标记位 ssplit_flag dict_flag
 */
public interface ArticleMapper {

    void batchInsert(List<Article> article);

    void insert(Article article);

    Article queryById(String pmid);

    /**
     * 剩余待处理 分页
     * @param col 数据库表列名（是否进行过某任务的标记位）
     * @param start
     * @param pagesize
     * @return
     */
    List<Article> queryLeftPage(@Param("col")String col, @Param("start") int start, @Param("pagesize") int pagesize);

    /**
     * 剩余待处理的
     * @return
     */
    Integer queryLeftTotal(String col);
}
