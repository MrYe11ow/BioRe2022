package com.example.demo.controller;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.pojo.Article;
import com.example.demo.pojo.Entity;
import com.example.demo.service.Result;
import com.example.vo.EchartsResponse;
import com.example.vo.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Result result;

    /**
     * 根据pmid查文章
     * @param pmid
     * @return
     */
    @RequestMapping("/article/{pmid}")
    public Article getArticleByPmid(@PathVariable("pmid") String pmid){
        return articleMapper.queryById(pmid);
    }

    /**
     * 获取包含某实体的所有文章id
     * @param name
     * @return
     */
    @RequestMapping("/contained/{name}")
    public List<String> getContainedPmids(@PathVariable("name") String name){
        return result.getContainedPmids(name);
    }

    /**
     * 获取包含某实体的所有文章id
     * @param name1
     * @param name2
     * @return
     */
    @RequestMapping("/contained/{name1}/{name2}")
    public List<String> getContainedPmids(@PathVariable("name1") String name1, @PathVariable("name2") String name2){
        return null;
    }

    /**
     * 获取指定文中的所有gene
     * @param pmid
     * @return
     */
    @RequestMapping("/gene/{pmid}")
    public List<Entity> getGeneByPmid(@PathVariable("pmid") String pmid){
        return null;
    }

    /**
     * 获取指定文中的所有protein
     * @param pmid
     * @return
     */
    @RequestMapping("/protein/{pmid}")
    public List<Entity> getProteinByPmid(@PathVariable("pmid") String pmid){
        return null;
    }

    /**
     * 请求表格数据
     * @return
     */
    @RequestMapping("/datagrid")
    public List<Row> datagrid(){
        return result.datagrid();
    }

    /**
     * 获取echarts所需的json数据
     * @return
     */
    @RequestMapping("/echarts")
    public EchartsResponse getEchartsData(){
        return result.buildEchartsData();
    }

}
