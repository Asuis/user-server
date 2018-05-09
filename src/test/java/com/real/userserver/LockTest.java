package com.real.userserver;

import com.qcloud.weapp.authorization.UserInfo;
import com.real.userserver.user.LoginService;
import com.real.userserver.user.aop.RedisDistributedLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {
    @Autowired
    RedisDistributedLock redisDistributedLock;

    @Autowired
    LoginService loginService;
    @Test
    public void test(){
        redisDistributedLock.lock("htest","000000",60);

        redisDistributedLock.unlock("htest","000000");
    }
    @Test
    public void test2(){
        UserInfo userInfo = new UserInfo();
        userInfo.setOpenId("dfdfdf");
        loginService.save(userInfo);
    }
}
