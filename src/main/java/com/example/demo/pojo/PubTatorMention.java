package com.example.demo.pojo;

import lombok.Data;

@Data
public class PubTatorMention {
    private int id;
    private String pmid;
    private int offset1;
    private int offset2;
    private String mention;
    private String type;

    public PubTatorMention() {
    }

    public PubTatorMention(String pmid, int offset1, int offset2, String mention, String type) {
        this.pmid = pmid;
        this.offset1 = offset1;
        this.offset2 = offset2;
        this.mention = mention;
        this.type = type;
    }
}
