package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.SentenceMapper;
import com.example.demo.pojo.Article;
import com.example.demo.pojo.Sentence;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用Apache OpenNLP进行分句
 */
@Service
public class SsplitService {

    String modelPath = "C:\\Users\\Administrator\\Downloads\\opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin";

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SentenceMapper sentenceMapper;

    public void splitAll(){
        SentenceModel model = null;
        try {
            InputStream is = new FileInputStream(modelPath);
            model = new SentenceModel(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceDetectorME detector = new SentenceDetectorME(model);

        Integer total = articleMapper.queryTotal();
        for(int i = 0; i<total; i+=500){
            List<Article> articles = articleMapper.queryPage(i, 500);
            for(Article article : articles){
                List<Sentence> sentenceList = new ArrayList<>();
                String pmid = article.getPmid();
                String[] sens = detector.sentDetect(article.getAbstractText());
                for(int j = 0; j< sens.length; j++){
                    sentenceList.add(new Sentence(pmid,j+1,sens[j]));
                }
                sentenceMapper.batchInsert(sentenceList);
            }

        }
    }

    public String[] split(String paragraph){
        SentenceModel model = null;
        try {
            InputStream is = new FileInputStream(modelPath);
            model = new SentenceModel(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceDetectorME detector = new SentenceDetectorME(model);
        String[] sens = detector.sentDetect(paragraph);
        return sens;
    }
}
