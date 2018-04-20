package com.real.userserver.user.dao;

import com.real.userserver.user.dto.OurUserInfo;
import com.real.userserver.user.model.UserAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author asuis
 */
@Mapper
public interface UserDao {
    /**
     * 判断用户是否第一次登录
     * @param openId 微信小程序openId
     * @return 存在openId的行数 >0
     * 说明该用户不是第一次登录
     * */
    public UserAuth isHaveUserAuthByOpenId(String openId);

    public OurUserInfo getOurUserInfoByOpenId(String openId);
}
