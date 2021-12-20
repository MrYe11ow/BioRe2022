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

        //todo 为啥Sentence表中有重复的句子
        Integer total = articleMapper.queryLeftTotal("ssplit_flag");
        int pagesize = 500;
        for(int i = 0; i<total; i+=pagesize){
            List<Sentence> sentenceList = new ArrayList<>();
            List<Article> articles = articleMapper.queryLeftPage("ssplit_flag", i, pagesize);
            for(Article article : articles){
                String pmid = article.getPmid();
                String title = article.getArticleTitle();
                sentenceList.add(new Sentence(pmid,0,title));
                String abstractText = article.getAbstractText();
                if(abstractText!=null){
                    String[] sens = detector.sentDetect(abstractText);
                    for(int j = 0; j< sens.length; j++){
                        sentenceList.add(new Sentence(pmid,j+1,sens[j]));
                    }
                }
            }
            sentenceMapper.batchInsert(sentenceList);
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
