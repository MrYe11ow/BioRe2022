package com.example.demo.service;

import com.example.demo.mapper.EntityMapper;
import com.example.demo.pojo.Entity;
import com.example.vo.EchartsLink;
import com.example.vo.EchartsNode;
import com.example.vo.EchartsResponse;
import com.example.vo.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class Result {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加载到缓存
     */
    public void loadCache(){
        //加载前先清空
        redisTemplate.delete(redisTemplate.keys("*"));

        List<Entity> entities = entityMapper.queryAll();
        for(Entity entity : entities){
            int id = entity.getId();
            String pmid = entity.getPmid();
            int serialNumber = entity.getSerialNumber();
            String name = entity.getName();
            String type = entity.getType();

            //存一句中的实体
            String sentenceKey = pmid + ":" + serialNumber;
            redisTemplate.opsForList().leftPush(sentenceKey,name);
            //存一文中的实体
            String articleKey = pmid;
            redisTemplate.opsForList().leftPush(articleKey,name);

            //todo 去重 统计某一实体在所有文章中出现的频次

            //统计某一实体在所有句子中出现的频次
            redisTemplate.opsForZSet().add("GeneSingleRanking",name,1);

            //哪些文章包含该实体 key[A:XXX] article
            redisTemplate.opsForSet().add("A:"+name,pmid);

            //哪些句子包含该实体 key[S:XXX] sentence
            redisTemplate.opsForSet().add("S:"+name,pmid+":"+serialNumber);
        }

        //todo 移上去
        //计算实体对的出现频次
        Set<String> keys = redisTemplate.keys("list*");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            List<String> list = redisTemplate.opsForList().range(key, 0, -1);
            if(list.size()>1){
                for(int i = 0; i< list.size()-1; i++){
                    for(int j = 0; j<list.size(); j++){
                        String e1 = list.get(i);
                        String e2 = list.get(j);
                        if(e1.compareTo(e2) > 0){
                            String temp = e1;
                            e1 = e2;
                            e2 = temp;
                        }
                        redisTemplate.opsForZSet().add("GenePairRanking",e1+":"+e2,1);
                    }
                }
            }
        }

        log.info("加载缓存完成");
    }

    public List<String> topSingle(int num){
        Set ranking = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("GeneSingleRanking", 0, num);
        return new ArrayList<>(ranking);
    }

    public List<String> topPair(int num){
        Set ranking = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("GenePairRanking", 0, num);
        return new ArrayList<>(ranking);
    }

    public List<String> getContainedPmids(String name){
        return redisTemplate.opsForList().range("AR:" + name, 0, -1);
    }

    public List<Row> datagrid(){
        List<Row> list = new ArrayList<>();
        return list;
    }

    public EchartsResponse buildEchartsData(){
        EchartsResponse response = new EchartsResponse();
        Set<EchartsNode> nodeSet = new HashSet<>();
        List<EchartsLink> linkList = new ArrayList<>();
        List<String> pairList = topPair(10);
        for(String pair : pairList){
            String[] ss = pair.split(":");
            String e1 = ss[0];
            String e2 = ss[1];
            int e1num = 0;//e1出现的次数
            int e2num = 0;//e2出现的次数
            int id1 = 0;//selectIdByName
            int id2 = 0;
            int category1 = 0;
            int category2 = 0;
            nodeSet.add(new EchartsNode(id1,e1,e1num,e1num,category1));
            nodeSet.add(new EchartsNode(id2,e2,e2num,e2num,category2));
            linkList.add(new EchartsLink(id1, id2));
        }
        return response;
    }
}
