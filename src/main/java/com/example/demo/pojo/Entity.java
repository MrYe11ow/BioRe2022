package com.example.demo.pojo;

import lombok.Data;

@Data
public class Entity {
    private int id;
    private String pmid;
    private int sentenceId;
    private int serialNumber;
    private String name;
    private String type;//gene、protein
    private String taggedby;//dict、abner

    public Entity(String pmid, int sentenceId, int serialNumber, String name, String type, String taggedby) {
        this.pmid = pmid;
        this.sentenceId = sentenceId;
        this.serialNumber = serialNumber;
        this.name = name;
        this.type = type;
        this.taggedby = taggedby;
    }
}
