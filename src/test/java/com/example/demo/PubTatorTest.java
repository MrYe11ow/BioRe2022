package com.example.demo;

import com.example.demo.service.PubTator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PubTatorTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PubTator pubTator;
    @Test
    public void test() throws IOException {
        List<String> list= new ArrayList<>();
        list.add("28483577");
        pubTator.tag(list);
    }
}
