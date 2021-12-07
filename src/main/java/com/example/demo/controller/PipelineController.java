package com.example.demo.controller;

import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Eutilities下载
     */
    @RequestMapping("/download")
    public void download(){
        String query = "breast+cancer+1935[pdat]";
        eutilities.download(query);
    }

    /**
     * Aphache OpenNLP分句
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
        Map<String, Integer> dict = dictBuilder.bulidFromMysql();
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
