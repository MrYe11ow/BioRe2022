package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.EntityMapper;
import com.example.demo.mapper.SentenceMapper;
import com.example.demo.pojo.Article;
import com.example.demo.pojo.Entity;
import com.example.demo.pojo.Sentence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DictMatcher {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SentenceMapper sentenceMapper;
    @Autowired
    private EntityMapper entityMapper;

    public void matchArticle(Map<String,Integer> dict){
        Integer total = articleMapper.queryLeftTotal("dict_flag");
        for(int i = 0; i< total; i+= 500){
            List<Article> articles = articleMapper.queryLeftPage("dict_flag",0,1000);
            for(Article article : articles){
                doMatch(dict, article.getPmid(), -1,-1,article.getAbstractText());
            }
        }
    }

    public void matchSentence(Map<String,Integer> dict){
        Integer total = sentenceMapper.queryLeftTotal("dict_flag");
        int pagesize = 2000;
        for(int i = 0; i< total; i+= pagesize){
            List<Sentence> sentences = sentenceMapper.queryLeftPage("dict_flag",0,pagesize);
            for(Sentence sentence : sentences){
                doMatch(dict, sentence.getPmid(), sentence.getId(), sentence.getSerialNumber(), sentence.getText());
            }
            log.info("{}/{}句子dict已处理",i,total);
        }
    }

    /**
     * 字典中的词都是单个的
     * @param dict
     * @param pmid
     * @param text
     */
    public void doMatch(Map<String,Integer> dict, String pmid, int sentenceId,int serialNumber, String text){
        //todo 可以替换为使用分词工具分词
        String[] words = text.split("\\s");
        int length = words.length;
        Set<String> tempSet = new HashSet<>();
        for(int i = 0; i < length; i++){
            String word = words[i];
            String upperCase = word.toUpperCase();
            if(dict.containsKey(upperCase)){
                tempSet.add(upperCase);
            }
        }
        if(!tempSet.isEmpty()){
            for(String e : tempSet){
                Entity entity = new Entity(pmid, sentenceId, serialNumber, e, "GENE", "DICT");
                entityMapper.insert(entity);
            }
        }
    }
}
