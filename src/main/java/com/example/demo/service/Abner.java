package com.example.demo.service;

import abner.Tagger;
import com.example.demo.mapper.EntityMapper;
import com.example.demo.mapper.SentenceMapper;
import com.example.demo.pojo.Entity;
import com.example.demo.pojo.Sentence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class Abner{

    private static String col = "abner_flag";

    @Autowired
    private SentenceMapper sentenceMapper;
    @Autowired
    private EntityMapper entityMapper;

    public void ner(){
        Tagger tagger = new Tagger();
        int total = sentenceMapper.queryLeftTotal(col);
        int pagesize = 500;
        for(int i = 0;i<total;i+=pagesize){
            List<Sentence> sentences = sentenceMapper.queryLeftPage(col, i, pagesize);
            for(Sentence sentence : sentences){
                doo(tagger, sentence.getPmid(), sentence.getId(), sentence.getSerialNumber(), sentence.getText());
            }
            log.info("{}/{}句子abner已处理",i,total);
        }
    }

    public void doo(Tagger tagger, String pmid, int sentenceId, int serialNumber, String text){
        String[][] tagged = tagger.getEntities(text);
        String[] entities = tagged[0];
        String[] types = tagged[1];
        if(entities.length != 0){
            for(int i = 0; i< entities.length; i++){
                String name = entities[i];
                String type = types[i];
                //todo 一句 只保存 同名实体 一次
                entityMapper.insert(new Entity(pmid,sentenceId,serialNumber, name,type,"ABNER"));
            }
        }
    }

}
