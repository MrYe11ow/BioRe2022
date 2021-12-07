package com.example.echarts;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EchartsResponse implements Serializable {

    private List<EchartsNode> nodes;
    private List<Map<Integer,Integer>> links;
    private List<Map<String,String>> categories;

}
