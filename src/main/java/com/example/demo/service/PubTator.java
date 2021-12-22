package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.PubTatorMentionMapper;
import com.example.demo.pojo.Entity;
import com.example.demo.pojo.PubTatorMention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PubTator {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PubTatorMentionMapper pubTatorMentionMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public void doNer() throws Exception{
        Integer total = articleMapper.queryLeftTotal("pubtator");
        int pagesize = 10000;
        for(int i = 0; i<total ;i+=pagesize){
            List<String> pmidList = articleMapper.getPmidList(i, pagesize);
            tag(pmidList);
        }
    }

    public void tag(List<String> pmidList) throws IOException {
        String url = "https://www.ncbi.nlm.nih.gov/research/pubtator-api/publications/export/pubtator";
        HttpHeaders headers = new HttpHeaders();
        JSONObject jsonObject = new JSONObject();
        List<String> types = new ArrayList<>();
        types.add("gene");
        jsonObject.put("pmids",pmidList);
        jsonObject.put("concepts",types);

        HttpEntity httpEntity = new HttpEntity(jsonObject,headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Resource> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Resource.class);

        BufferedReader br = null;
        try {
            InputStream inputStream = response.getBody().getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        List<PubTatorMention> list= new ArrayList<>();
        while((line = br.readLine())!=null){
            //String regex = "[0-9]+\t[0-9]+\t[0-9]+\t";
            String[] ss = line.split("\t");
            if(ss.length == 5){
                String pmid = ss[0];
                String mention = ss[3];
                String type = ss[4];
                int offset1 = Integer.parseInt(ss[1]);
                int offset2 = Integer.parseInt(ss[2]);
                PubTatorMention pubTatorMention = new PubTatorMention(pmid, offset1, offset2, mention, type);
                list.add(pubTatorMention);
                if(list.size() % 10000 == 0){
                    pubTatorMentionMapper.batchInsert(list);
                    list.clear();
                }
            }

        }
        if(list.size()>0){
            pubTatorMentionMapper.batchInsert(list);
        }
    }
}
