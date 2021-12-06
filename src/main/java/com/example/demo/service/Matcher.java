package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.SentenceMapper;
import com.example.demo.pojo.Article;
import com.example.demo.pojo.Sentence;
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
    @Autowired
    private SentenceMapper sentenceMapper;

    public void matchArticle(Map<String,Integer> dict){
        Integer total = articleMapper.queryTotal();
        for(int i = 0; i< total; i+= 500){
            List<Article> articles = articleMapper.queryPage(0,1000);
            for(Article article : articles){
                doMatch(dict, article.getPmid(), article.getAbstractText());
            }
        }
    }

    public void matchSentence(Map<String,Integer> dict){
        Integer total = sentenceMapper.queryTotal();
        for(int i = 0; i< total; i+= 2000){
            List<Sentence> sentences = sentenceMapper.queryPage(0,1000);
            for(Sentence sentence : sentences){
                doMatch(dict, sentence.getPmid(), sentence.getText());
            }
        }
    }

    /**
     * 字典中的词都是单个的
     * @param dict
     * @param pmid
     * @param text
     */
    public void doMatch(Map<String,Integer> dict, String pmid, String text){
        String[] words = text.split("\\s");
        int length = words.length;
        for(int i = 0; i < length; i++){
            String word = words[i];
            String upperCase = word.toUpperCase();
            if(dict.containsKey(upperCase)){
                String sentenceKey = pmid + ":" + (i+1);
                redisTemplate.opsForList().leftPush(sentenceKey,word);
                String articleKey = pmid;
                redisTemplate.opsForList().leftPush(articleKey,word);

                //统计某一实体在所有文章中出现的频次

                //统计某一实体在所有句子中出现的频次
                redisTemplate.opsForZSet().add("GeneSingleZSet",upperCase,1);

                //哪些文章包含该实体 key[AR:XXX] article
                redisTemplate.opsForSet().add("AR:"+upperCase,pmid);

                //哪些句子包含该实体 key[SE:XXX] sentence
                redisTemplate.opsForSet().add("SE:"+upperCase,pmid);
            }
        }
    }
}
