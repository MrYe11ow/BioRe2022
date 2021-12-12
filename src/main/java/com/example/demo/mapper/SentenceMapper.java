package com.example.demo.mapper;

import com.example.demo.pojo.Sentence;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 标记位 dict_flag abner_flag
 */
public interface SentenceMapper {

    void insert(Sentence sentence);

    void batchInsert(List<Sentence> sentences);

    Sentence queryById(Long id);

    List<Sentence> queryByPmid(String pmid);

    /**
     * 该步骤中 剩余 分页
     * @param col 列名 任务完成标记位
     * @param start
     * @param pagesize
     * @return
     */
    List<Sentence> queryLeftPage(@Param("col")String col, @Param("start")int start, @Param("pagesize")int pagesize);

    /**
     * 该步骤中 剩余待处理数量
     * @param col
     * @return
     */
    int queryLeftTotal(String col);

    List<Sentence> coccurrence(@Param("e1") String e1, @Param("e2") String e2);
}
