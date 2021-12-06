package com.example.demo.mapper;

import com.example.demo.pojo.Sentence;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SentenceMapper {

    void insert(Sentence sentence);

    void batchInsert(List<Sentence> sentences);

    Sentence queryById(Long id);

    List<Sentence> queryByPmid(String pmid);

    List<Sentence> queryPage(@Param("start")int start, @Param("pagesize")int pagesize);

    int queryTotal();
}
