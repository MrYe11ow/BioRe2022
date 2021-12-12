package com.example.demo.controller;

import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 该类下所有方法都只需执行一次
 */
@RestController
@RequestMapping("/pipeline")
public class PipelineController {

    @Autowired
    private Eutilities eutilities;
    @Autowired
    private DictBuilder dictBuilder;
    @Autowired
    private SsplitService ssplitService;
    @Autowired
    private DictMatcher dictMatcher;
    @Autowired
    private Abner abner;
    @Autowired
    private Result result;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/downloadtest")
    public void downloadTest(){
        String webEnv = "MCID_61b43102f55f07185f4bb55e";
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&WebEnv={1}&query_key={2}&retstart={3}&retmax={4}&retmode=xml";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, webEnv, "1", 0, 500);
        String body = responseEntity.getBody();
        System.out.println(body);
    }

    /**
     * Eutilities下载
     * 保存整篇文章
     */
    @RequestMapping("/download")
    public void download(){
        //todo 为Article Sentence表 pmid 建索引
//        String query = "breast+cancer+1935[pdat]";
        String query = "breast+cancer+2021[pdat]";
        eutilities.download(query);
    }

    /**
     * Aphache OpenNLP分句
     * 保存分句的结果
     * 3w 篇 单线程处理约 30s
     */
    @RequestMapping("/ssplit")
    public void ssplit(){
        ssplitService.splitAll();
    }

    /**
     * 字典法匹配识别gene
     * @throws Exception
     */
    @RequestMapping("/dictner")
    public void dictner() throws Exception{
        Map<String, Integer> dict = dictBuilder.bulidFromMysql("gene");
        dictMatcher.matchSentence(dict);
    }

    /**
     * abner识别蛋白质等
     */
    @RequestMapping("/abner")
    public void abner(){
        abner.ner();
    }

    /**
     * 将识别结果从mysql加载到redis，方便统计
     */
    @RequestMapping("/loadcache")
    public void loadcache(){
        result.loadCache();
    }

}
