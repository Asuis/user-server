package com.real.userserver;

import com.qcloud.weapp.authorization.UserInfo;
import com.real.userserver.qrcode.service.GenerateIdService;
import com.real.userserver.user.mapper.UserInfoMapper;
import com.real.userserver.user.model.UserDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServerApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(UserServerApplicationTests.class);

	@Autowired
	UserInfoMapper userInfoMapper;
	@Resource
	private RedisTemplate<String,Object> redisTemplate;

	@Autowired
	private GenerateIdService generateIdServiceImpl;
	@Test
	public void test(){
		System.out.println("++"+generateIdServiceImpl.generateId());
	}
	@Test
	public void contextLoads() {
		UserInfo userInfo = new UserInfo();
		userInfo.setAvatarUrl("avatar");
		userInfo.setCity("hello");
		userInfo.setCountry("hhhh");
		userInfo.setGender(1);
		userInfo.setLanguage("hh");
		userInfo.setOpenId("hhhhhhhhh");
		UserDetail userDetail = userInfoMapper.USER_INFO_MAPPER.userInfoToUserDetail(userInfo);
		logger.info(userDetail.getAvatarUrl());
	}

}
