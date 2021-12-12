package com.example.demo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sentence {
    private int id;
    private String pmid;
    private int serialNumber;
    private String text;

    public Sentence(String pmid, int serialNumber, String text){
        this.pmid = pmid;
        this.serialNumber = serialNumber;
        this.text = text;
    }
}
