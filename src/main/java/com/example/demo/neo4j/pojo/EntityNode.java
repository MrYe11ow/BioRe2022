package com.example.demo.neo4j.pojo;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * 参考链接：
 * https://gitee.com/tonels/MultiProject/tree/master/boot-neo4j
 * https://blog.csdn.net/weixin_42952909/article/details/117990223
 */
@NodeEntity(label = "entity")
@Data
public class EntityNode {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
