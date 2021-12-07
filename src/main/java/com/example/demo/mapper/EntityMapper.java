package com.example.demo.mapper;

import com.example.demo.pojo.Entity;

import java.util.List;

public interface EntityMapper {

    void insert(Entity entity);

    void batchInsert(List<Entity> list);

    List<Entity> queryByPmid(String pmid);

    List<Entity> queryBySentenceId(Long id);

    List<Entity> queryAll();
}
