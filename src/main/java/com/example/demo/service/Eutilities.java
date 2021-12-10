package com.example.demo.service;

import com.example.demo.pojo.EfetchParam;
import com.example.demo.util.JsoupUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

@Slf4j
@Service
public class Eutilities {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 数据请求格式定义
     * https://www.ncbi.nlm.nih.gov/books/NBK25498/#chapter3.Application_3_Retrieving_large
     *
     * 使用多线程下载
     * 线程数 corePoolSize,根据等待队列长度控制线程池任务提交速度，防止任务堆积过多造成异常
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
        ThreadPoolExecutor pool = new ThreadPoolExecutor(20,30,30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(50));
        for(int i = 0; i < efetchParam.getTotal(); i+=retmax){
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int queueSize = pool.getQueue().size();
                if(queueSize < 40)
                    break;
            }
            pool.execute(new DataTask(url2, efetchParam.getWebEnv(), efetchParam.getKey(), i,retmax));
        }
    }
}
