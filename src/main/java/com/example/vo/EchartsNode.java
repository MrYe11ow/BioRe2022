package com.example.vo;

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

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;
        if(object == null)
            return false;
        if(getClass() != object.getClass())
            return false;
        EchartsNode node = (EchartsNode) object;
        if(name.equals(node.getName()))
            return true;
        else return false;
    }
}
