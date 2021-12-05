package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public class Matcher {

    @Autowired
    private RedisTemplate redisTemplate;

    public void match(Map<String,Integer> dict, String pmid, String text){
        String[] words = text.split("\\s");
        int length = words.length;
        for(int i = 0; i < length; i++){
            String word = words[i];
            String lowerCase = word.toLowerCase();
            boolean b = dict.containsKey(lowerCase);
            if(b){
                String key = pmid + ":" + (i+1);
                redisTemplate.opsForList().leftPush(key,word);
            }
        }
    }
}
