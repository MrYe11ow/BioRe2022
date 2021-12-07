package com.example.demo.service;

import abner.Tagger;
import com.example.demo.demo.Text;
import com.example.demo.mapper.EntityMapper;
import com.example.demo.mapper.SentenceMapper;
import com.example.demo.pojo.Entity;
import com.example.demo.pojo.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Abner{

    private static String col = "abner_flag";

    @Autowired
    private SentenceMapper sentenceMapper;
    @Autowired
    private EntityMapper entityMapper;

    private Tagger tagger;

    public void ner(){
        int total = sentenceMapper.queryLeftTotal(col);
        for(int i = 0;i<total;i+=500){
            List<Sentence> sentences = sentenceMapper.queryLeftPage(col, i, 500);
            for(Sentence sentence : sentences){
                doo(sentence.getPmid(), sentence.getId(), sentence.getSerialNumber(), sentence.getText());
            }
        }
    }

    public void doo(String pmid, int sentenceId, int serialNumber, String text){
        String[][] tagged = tagger.getEntities(text);
        String[] entities = tagged[0];
        String[] types = tagged[1];
        if(entities.length != 0){
            for(int i = 0; i< entities.length; i++){
                String name = entities[i];
                String type = types[i];

                entityMapper.insert(new Entity(pmid,sentenceId,serialNumber, name,type,"ABNER"));
            }
        }

    }

}
