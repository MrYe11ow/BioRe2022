package com.example.demo.mapper;

import com.example.demo.pojo.Entity;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityMapper {

    void insert(Entity entity);

    void batchInsert(List<Entity> list);

    List<Entity> queryByPmid(String pmid);

    List<Entity> queryBySentenceId(Long id);

    List<Entity> queryAll();

    int total();

    List<Entity> queryByPage(@Param("start")int start,@Param("pagesize")int pagesize);

}
