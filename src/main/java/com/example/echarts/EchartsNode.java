package com.example.echarts;

import lombok.Data;

import java.io.Serializable;

@Data
public class EchartsNode implements Serializable {
    private int id;
    private String name;
    private int symbolSize;
    private int value;
    private int category;

    public EchartsNode(int id, String name, int symbolSize, int value, int category) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
        this.value = value;
        this.category = category;
    }
}
