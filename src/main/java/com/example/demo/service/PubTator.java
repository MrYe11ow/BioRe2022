package com.example.demo.service;

import com.example.demo.pojo.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;

@Service
public class PubTator {

    @Autowired
    private RestTemplate restTemplate;

    public void tag(List<String> pmidList) throws IOException {
        String url = "https://www.ncbi.nlm.nih.gov/research/pubtator-api/publications/export/pubtator";
        HttpHeaders headers = new HttpHeaders();
        Object obj = new Object();
        HttpEntity httpEntity = new HttpEntity(obj,headers);
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
        while((line = br.readLine())!=null){
            //String regex = "[0-9]+\t[0-9]+\t[0-9]+\t";
            String[] ss = line.split("\t");
            if(ss.length == 5){
                String pmid = ss[0];
                String mention = ss[3];

            }
        }
    }
}
