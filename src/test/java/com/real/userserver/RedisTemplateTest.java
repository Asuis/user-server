package com.real.userserver;

import com.real.userserver.qrcode.service.GenerateIdService;
import com.real.userserver.qrcode.service.impl.GenerateIdServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplateTest.class);
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private GenerateIdService generateIdService;
//    @Test
//    public void getGenerateIdService() {
//        long a = generateIdService.generateId();
//        logger.info("hehfle:::",a);
//    }

    @Test
    public void incrTest(){
        try {
            redisTemplate.opsForHash().increment("gen_id","gen",1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
