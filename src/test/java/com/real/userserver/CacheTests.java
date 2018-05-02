package com.real.userserver;

import com.alibaba.fastjson.JSON;
import com.real.userserver.dto.Result;
import com.real.userserver.user.LoginServiceImpl;
import com.real.userserver.user.dto.SimpleUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CacheTests {
    @Autowired
    LoginServiceImpl loginService;

    @Test
    public void test() {
        Result<SimpleUserInfo> result = loginService.getUserSimpleInfo("oAgY65Dqtd-8Zg4GoQ_7HCZ6cYUA");
        log.info(JSON.toJSONString(result));
        Result<SimpleUserInfo> result2 = loginService.getUserSimpleInfo("oAgY65Dqtd-8Zg4GoQ_7HCZ6cYUA");
        log.info(JSON.toJSONString(result2));
        Result<SimpleUserInfo> result3 = loginService.getUserSimpleInfo("oAgY65Dqtd-8Zg4GoQ_7HCZ6cYUA");
        log.info(JSON.toJSONString(result3));
    }
}
