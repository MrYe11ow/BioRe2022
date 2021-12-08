package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class EchartsResponse implements Serializable {

    private Set<EchartsNode> nodes;
    private List<EchartsLink> links;
    private List<EchartsCategory> categories;

    public EchartsResponse(){
        categories = new ArrayList<>();
        categories.add(new EchartsCategory("gene"));
        categories.add(new EchartsCategory("protein"));
        categories.add(new EchartsCategory("chemd"));
    }
}
