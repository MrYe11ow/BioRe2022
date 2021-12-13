package com.example.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class StatInfoVo {
    private int articleCount;
    private int sentenceCount;
    private List<Map<String,Object>> entityStatInfo;
}
