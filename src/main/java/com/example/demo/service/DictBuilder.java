package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DictBuilder {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Map<String,Integer> bulidFromTxt(String filePath)throws Exception{
        int rowNum = 0;
        Map<String,Integer> dict = null;
        try {
            long count = Files.lines(Paths.get(new File(filePath).getPath())).count();
            rowNum = (int)count;
        } catch (IOException e) {
            throw new Exception("No File Found");
        }
        if(rowNum != 0){
            dict = new HashMap<String,Integer>(rowNum);
        }
        if(dict == null){
            throw new Exception("词典创建失败");
        }
        try(FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line=br.readLine()) != null){
                dict.put(line,0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dict;
    }

    public Map<String,Integer> bulidFromMysql(String type)throws Exception{
        String dictName = null;
        switch (type){
            case "gene":
                dictName = "HGNC";
                break;
            default:
                break;
        }
        if(dictName == null){
            throw new Exception("未找到匹配的词典");
        }
        String sql = "select count(1) from " + dictName;
        Integer totalNum = jdbcTemplate.queryForObject(sql, Integer.class);
        int mapInitSize = (int) ((float) totalNum / 0.75F + 1.0F);
        log.info("map的初始化大小为：" + mapInitSize);
        Map<String,Integer> dict = null;
        if(mapInitSize > 0){
            dict = new HashMap<String,Integer>(mapInitSize);
        }
        String querySql = "select Approved_symbol from " + dictName;
        try {
            List<String> list = jdbcTemplate.queryForList(querySql, null, String.class);
            for(String s : list){
                dict.put(s,0);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new Exception("词典创建失败");
        }
        log.info("词典初始化完毕");
        return dict;
    }

    public static Map<String,Integer> buildFromExcel(){
        return null;
    }

    public static Map<String,Integer> buildFromRedis(){
        return null;
    }
}
