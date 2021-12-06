package com.example.demo.service;

import abner.Tagger;
import com.example.demo.demo.Text;
import org.springframework.stereotype.Service;

@Service
public class Abner {

    private static Tagger tagger = new Tagger();

    public void ner(String pmid, String serialNum, String text){
        String[][] tagged = tagger.getEntities(text);
        String[] entities = tagged[0];
        String[] types = tagged[1];
        if(entities.length != 0){
            for(int i = 0; i< entities.length; i++){
                String entity = entities[i];
                String type = types[i];
            }
        }

    }
}
