package com.example.demo.neo4j.impl;

import com.example.demo.neo4j.GraphService;
import com.example.demo.neo4j.pojo.EntityNode;
import com.example.demo.neo4j.pojo.Relation;
import com.example.demo.neo4j.repository.EntityRepository;
import com.example.demo.neo4j.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GraphServiceImpl implements GraphService {

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Override
    public List<EntityNode> findByName(String name) {
        return entityRepository.findByName(name);
    }

    @Override
    public void saveEntity(EntityNode entityNode) {
        entityRepository.save(entityNode);
    }

    @Override
    public void saveRelation(String subName, String objName) {
        EntityNode sub = entityRepository.findByName(subName).get(0);
        EntityNode obj = entityRepository.findByName(objName).get(0);
        relationRepository.save(new Relation(sub, obj));
    }
}
