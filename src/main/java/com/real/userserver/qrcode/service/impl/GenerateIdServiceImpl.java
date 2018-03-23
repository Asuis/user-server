package com.real.userserver.qrcode.service.impl;

import com.real.userserver.qrcode.service.GenerateIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

/**
 * @author asuis
 */
@Service
public class GenerateIdServiceImpl implements GenerateIdService {

    private final static Logger logger = LoggerFactory.getLogger(GenerateIdServiceImpl.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public Long generateId() {
        return incrementHash("gen_id","gen",1L);
    }
    /**
     * 获取唯一Id
     * @param key redis键名
     * @param hashKey 保存hash表的键
     * @param delta 增加量（不传采用1）
     * @return 唯一id
     */
    private Long incrementHash(String key,String hashKey,Long delta) {
        try {
            if (null == delta) {
                delta=1L;
            }
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {//redis宕机时采用uuid的方式生成唯一id
            int first = new Random(10).nextInt(8) + 1;
            int randNo= UUID.randomUUID().toString().hashCode();
            if (randNo < 0) {
                randNo=-randNo;
            }
            return Long.valueOf(first + String.format("%16d", randNo));
        }
    }
}
