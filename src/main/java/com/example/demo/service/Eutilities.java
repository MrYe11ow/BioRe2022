package com.example.demo.service;

import com.example.demo.pojo.EfetchParam;
import com.example.demo.util.JsoupUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class Eutilities {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 格式定义
     * https://www.ncbi.nlm.nih.gov/books/NBK25498/#chapter3.Application_3_Retrieving_large
     * @param query
     */
    public void download(String query){
        String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
        String url1 = base + "esearch.fcgi?db=pubmed&term=" + query + "&usehistory=y";
        String html =  restTemplate.getForEntity(url1, String.class).getBody();
        EfetchParam efetchParam = JsoupUtil.parseForEfetchParam(html);
        int retmax = 500;
        log.info("即将开始请求{}篇数据",efetchParam.getTotal());
        String url2 = base +"efetch.fcgi?db=pubmed&WebEnv={1}&query_key={2}&retstart={3}&retmax={4}&retmode=xml";
        ExecutorService pool = Executors.newFixedThreadPool(16);
        for(int i = 0; i < efetchParam.getTotal(); i+=retmax){
            pool.execute(new DataTask(url2, efetchParam.getWebEnv(), efetchParam.getKey(), i,retmax));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
