package com.example.demo.service;

import com.example.demo.mapper.EntityMapper;
import com.example.demo.mapper.SentenceMapper;
import com.example.demo.pojo.Entity;
import com.example.demo.pojo.Sentence;
import com.example.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class Result {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private SentenceMapper sentenceMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 加载到缓存
     * <2min
     */
    public void loadCache(){
        //加载前先清空
        redisTemplate.delete(redisTemplate.keys("*"));

        //List<Entity> entities = entityMapper.queryAll();
        int total = entityMapper.total();
        int pagesize = 10000;
        for(int i =0; i<total;i+=pagesize){
            List<Entity> entities = entityMapper.queryByPage(i, pagesize);
            for(Entity entity : entities){
                int id = entity.getId();
                int sentenceId = entity.getSentenceId();
                String pmid = entity.getPmid();
                int serialNumber = entity.getSerialNumber();
                String name = entity.getName();
                String type = entity.getType();

                //存一句中的实体
                redisTemplate.opsForList().leftPush("Spmid:"+pmid+":"+serialNumber,name);
                //存一文中的实体
                redisTemplate.opsForList().leftPush("Apmid:"+pmid,name);

                /*//todo 去重 统计某一实体在所有文章中出现的频次

                //统计某一实体在所有句子中出现的频次
                redisTemplate.opsForZSet().add("SingleRanking",name,1);*/

                /*//哪些文章包含该实体 key[A:XXX] article
                redisTemplate.opsForSet().add("Aname:"+name,pmid);

                //哪些句子包含该实体 key[S:XXX] sentence
                redisTemplate.opsForSet().add("Sname:"+name,pmid+":"+serialNumber);*/
            }
        }

        log.info("加载缓存完成");
        System.out.println("加载缓存完成");
    }

    public List<Map<String, Object>> topSingle(String type,int limit){
//        Set ranking = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("GeneSingleRanking", 0, num);
//        return new ArrayList<>(ranking);

        List<Map<String, Object>> mapList = null;
        mapList = redisTemplate.opsForList().range("top10" + type, 0, -1);
        if(mapList == null || mapList.size()==0){
            String sql = "select name,count(1) as count from Entity where type = '"+type+"' group by name order by count(1) desc limit "+limit;
             mapList = jdbcTemplate.queryForList(sql);
             if(mapList !=null && mapList.size()>0)
            for(Map<String,Object> map : mapList){
                redisTemplate.opsForList().leftPush("top10"+type,map);
            }
        }
        return mapList;
    }

    public List<TopNPair> topPair(int num){
        Boolean b = redisTemplate.hasKey("PairRanking");
        if(!b){
            //计算实体对的出现频次
            Set<String> keys = redisTemplate.keys("Spmid*");
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                List<String> list = redisTemplate.opsForList().range(key, 0, -1);
                if(list.size()>1){
                    for(int i = 0; i< list.size()-1; i++){
                        for(int j = i+1; j<list.size(); j++){
                            String e1 = list.get(i);
                            String e2 = list.get(j);
                            if(e1.compareTo(e2) > 0){
                                String temp = e1;
                                e1 = e2;
                                e2 = temp;
                            }
                            //redisTemplate.opsForValue().increment(e1+":"+e2);
                            Double score = redisTemplate.opsForZSet().score("PairRanking", e1 + ":" + e2);
                            if(score == null){
                                score = 1.0;
                            }else {
                                score++;
                            }
                            redisTemplate.opsForZSet().add("PairRanking",e1+":"+e2,score);
                        }
                    }
                }
            }
            System.out.println("計算完成");
        }

        List<TopNPair> result = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("PairRanking", 0, Double.POSITIVE_INFINITY,0,50);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = set.iterator();
        while(iterator.hasNext()){
            ZSetOperations.TypedTuple<String> next = iterator.next();
            String pair = next.getValue();
            String[] ss = pair.split(":");
            if(ss[0].equals(ss[1])){
                continue;
            }
            TopNPair topNPair = new TopNPair(ss[0], "PROTEIN", ss[1], "PROTEIN", next.getScore());
            result.add(topNPair);
            if(result.size()==20){
                break;
            }
        }
        return result;
    }

    public List<String> getContainedPmids(String name){
        return redisTemplate.opsForList().range("AR:" + name, 0, -1);
    }

    public List<TopNSingle> datagrid(){
        List<TopNSingle> list = new ArrayList<>();
        return list;
    }

    public EchartsResponse buildEchartsData(){
        EchartsResponse response = new EchartsResponse();
        Set<EchartsNode> nodeSet = new HashSet<>();
        List<EchartsLink> linkList = new ArrayList<>();
        List<Map<String, Object>> topSingle = topSingle("PROTEIN",50);
        for(Map<String,Object> map: topSingle){
            //new EchartsNode();
        }


        List<TopNPair> pairList = topPair(10);
        for(TopNPair pair : pairList){
            String e1 = pair.getE1();
            String e2 = pair.getE2();
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
        response.setNodes(nodeSet);
        response.setLinks(linkList);
        //response.setCategories();
        return response;
    }

    public List<Sentence> getContainedSentences(String name1, String name2) {
        return sentenceMapper.coccurrence(name1, name2);
    }

    public StatInfoVo statInfo(){
        Map<String,Integer> map = new HashMap<>();
        String sql1 = "select count(1) from Article";
        String sql2 = "select count(1) from Sentence";
        String sql3 = "select type,count(1) as number from Entity group by type";
        Integer articleCount = jdbcTemplate.queryForObject(sql1, Integer.class);
        Integer sentenceCount = jdbcTemplate.queryForObject(sql2, Integer.class);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql3);
        StatInfoVo statInfoVo = StatInfoVo.builder()
                .articleCount(articleCount)
                .sentenceCount(sentenceCount)
                .entityStatInfo(maps)
                .build();
        return statInfoVo;
    }

    public int detailTotal(String name1,String name2){
        return sentenceMapper.countCoccurrence(name1,name2);
    }
}
