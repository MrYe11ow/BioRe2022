package com.example.demo.neo4j.pojo;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "entityRelation")
@Data
public class Relation {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @StartNode
    private EntityNode sub;

    @EndNode
    private EntityNode obj;

    public Relation(EntityNode sub, EntityNode obj){
        this.sub = sub;
        this.obj = obj;
    }
}
