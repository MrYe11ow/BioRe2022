package com.example.demo.controller;

import com.example.demo.service.DictBuilder;
import com.example.demo.service.Eutilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private Eutilities eutilities;

    @Autowired
    private DictBuilder dictBuilder;

    @RequestMapping("/download")
    public void download(){
        String query = "breast+cancer+1935[pdat]";
        eutilities.download(query);
    }

    @RequestMapping("/ner")
    public void ner() throws Exception{
        Map<String, Integer> dict = dictBuilder.bulidFromMysql();
    }

}
