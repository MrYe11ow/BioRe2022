package com.example.demo.mapper;

import com.example.demo.pojo.PubTatorMention;

import java.util.List;

public interface PubTatorMentionMapper {
    void batchInsert(List<PubTatorMention> list);
}
