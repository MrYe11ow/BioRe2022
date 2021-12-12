package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.pojo.Article;
import com.example.demo.util.JsoupUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//@Slf4j
public class DataTask implements Runnable{

    private RestTemplate restTemplate;
    private ArticleMapper articleMapper;

    private static AtomicInteger count;

    private String url;
    private String webEnv;
    private String key;
    private int retstart;
    private int retmax;

    public DataTask(RestTemplate restTemplate, ArticleMapper articleMapper,String url, String webEnv, String key, int retstart, int retmax){
        super();
        this.restTemplate = restTemplate;
        this.articleMapper = articleMapper;
        this.url = url;
        this.webEnv = webEnv;
        this.key = key;
        this.retstart = retstart;
        this.retmax = retmax;
    }

    @Override
    public void run() {
        long t1 = System.currentTimeMillis();
        String body = null;
        try {
            body = restTemplate.getForEntity(url, String.class, webEnv, key, retstart, retmax).getBody();
        } catch (RestClientException e) {
            //log.error("Eutilities下载失败,retstart={}",retstart);
            System.out.println("Eutilities下载失败,retstart="+retstart);
        }
        if(body != null){
            long t2 = System.currentTimeMillis();
            List<Article> list = JsoupUtil.parseForArticles(body);
            long t3 = System.currentTimeMillis();
            if(list != null && list.size() > 0){
                articleMapper.batchInsert(list);
                //log.info("{}篇,获取:{}ms,解析:{}ms,{}p篇已保存",retmax,(t2-t1),(t3-t2),count.addAndGet(list.size()));
                //todo 空指针异常
                System.out.println(retmax+"篇,获取:"+(t2-t1)+"ms,解析:"+(t3-t2)+"ms,"+count.addAndGet(list.size())+"p篇已保存");
            }
        }
    }
}
