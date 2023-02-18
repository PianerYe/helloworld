package com.itheima.helloworld.com.itheima.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作String类型数据
     */
    @Test
    public void testString() {
        redisTemplate.opsForValue().set("city123", "beijing123");
        String city = (String) redisTemplate.opsForValue().get("city123");
        System.out.println(city);

        redisTemplate.opsForValue().set("key1", "value1", 10l, TimeUnit.SECONDS);

        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city123", "nanjing");
        System.out.println(aBoolean);

    }

    /**
     * 操作Hash类型数据
     */
    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        //存值
        hashOperations.put("002", "name", "xiaoming");
        hashOperations.put("002", "age", "20");
        hashOperations.put("002", "addr", "beijing");

        //取值
        String age = (String) hashOperations.get("002", "age");
        System.out.println(age);

        //获取hash结构中的所有字段
        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }
        //获取hash结构中的所有值
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作list类型数据
     */
    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        //存值
        listOperations.leftPush("mylist","a");
        listOperations.leftPushAll("mylist","b","c","d");

        //取值
        List<String> mylist = listOperations.range("mylist", 0, -1);
        for (String value : mylist) {
            System.out.println(value);
        }
        //获得列表长度llen
        Long size = listOperations.size("mylist");
        int i1 = size.intValue();
        for (int i = 0; i < i1; i++) {
            //出队列
            String mylist1 = (String) listOperations.rightPop("mylist");
            System.out.println(mylist1);
        }

    }
    /**
     * 操作set类型数据
     */

    @Test
    public void testSst() {
        SetOperations setOperations = redisTemplate.opsForSet();

        //存值
        setOperations.add("myset","a","b","c","a");
        //取值
        Set<String> myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }
        //删除成员
        setOperations.remove("myset","a","b");

        //取值
        myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }
    }

    /**
     * 操作Zset类型数据
     */
    @Test
    public void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //存储
        zSetOperations.add("myZset","a",10.0);
        zSetOperations.add("myZset","b",11.0);
        zSetOperations.add("myZset","c",12.0);
        zSetOperations.add("myZset","a",13.0);
        //取值
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }
        System.out.println("--------------修改分数-----------");
        //修改分数
        zSetOperations.incrementScore("myZset","b",20);
        //取值
        myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }
        System.out.println("--------------删除成员-----------");
        //删除成员
        zSetOperations.remove("myZset","a","b");
        //取值
        myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }
    }

    /**
     * 通用操作，针对不同的数据类型斗可以操作
     */
    @Test
    public void testCommon(){
        //获取redis中所有的key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
        System.out.println("---------判断某个key是否存在------------");
        //判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);
        //删除指定key
        System.out.println("---------删除指定key------------");
        redisTemplate.delete("myZset");
        //获取指定key对应value的数据类型
        System.out.println("---------查看数据类型------------");
        DataType mySet = redisTemplate.type("myset");
        System.out.println(mySet.name());
    }
}