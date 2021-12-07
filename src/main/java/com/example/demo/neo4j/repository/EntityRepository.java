package com.example.demo.neo4j.repository;

import com.example.demo.neo4j.pojo.EntityNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface EntityRepository extends Neo4jRepository<EntityNode, Long> {

    @Query("MATCH c=(p:Entity) WHERE p.name=$name RETURN p")
    List<EntityNode> findByName(String name);
}
