package com.example.demo.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EfetchParam {
    private String webEnv;
    private String key;
    private int total;
}
