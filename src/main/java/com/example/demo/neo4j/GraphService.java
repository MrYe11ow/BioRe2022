package com.example.demo.neo4j;

import com.example.demo.neo4j.pojo.EntityNode;

import java.util.List;

public interface GraphService {

    List<EntityNode> findByName(String name);

    void saveEntity(EntityNode entityNode);

    void saveRelation(String subName, String objName);
}
