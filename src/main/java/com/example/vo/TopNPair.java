package com.example.vo;

import lombok.Data;

@Data
public class TopNPair {
    private String e1;
    private String e1type;
    private String e2;
    private String e2type;
    private double score;

    public TopNPair(String e1, String e1type, String e2, String e2type, double score) {
        this.e1 = e1;
        this.e1type = e1type;
        this.e2 = e2;
        this.e2type = e2type;
        this.score = score;
    }
}
