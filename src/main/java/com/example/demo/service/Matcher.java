package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Matcher {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ArticleMapper articleMapper;

    public void match(Map<String,Integer> dict){
        Integer total = articleMapper.queryTotal();
        for(int i = 0; i< total; i+= 1000){
            List<Article> articles = articleMapper.queryPage(0,1000);
            for(Article article : articles){
                doMatch(dict, article.getPmid(), article.getAbstractText());
            }
        }
    }

    public void doMatch(Map<String,Integer> dict, String pmid, String text){
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
