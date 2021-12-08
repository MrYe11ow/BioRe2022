package com.example.vo;

import lombok.Data;

@Data
public class EchartsLink {
    private int source;
    private int target;

    public EchartsLink(int source, int target) {
        this.source = source;
        this.target = target;
    }
}
