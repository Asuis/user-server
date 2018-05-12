package com.real.userserver.user;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;
import com.real.userserver.dto.Result;
import com.real.userserver.dto.ResultCode;
import com.real.userserver.user.aop.RedisLockable;
import com.real.userserver.user.dao.UserAuthMapper;
import com.real.userserver.user.dao.UserDao;
import com.real.userserver.user.dao.UserDetailMapper;
import com.real.userserver.user.dto.*;
import com.real.userserver.user.mapper.UserInfoMapper;
import com.real.userserver.user.model.UserAuth;
import com.real.userserver.user.model.UserDetail;
import com.real.userserver.user.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author asuis
 * 关于事务
 * Spring中的@Transactional基于动态代理的机制，提供了一种透明的事务管理机制，方便快捷解决在开发中碰到的问题。
 * Transactional(propagation=Propagation.NOT_SUPPORTED)
 * Propagation支持7种不同的传播机制：
 *
 * REQUIRED：如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。
 *
 * SUPPORTS： 如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。但是对于事务同步的事务管理器，PROPAGATION_SUPPORTS与不使用事务有少许不同。
 *
 * NOT_SUPPORTED：总是非事务地执行，并挂起任何存在的事务。
 *
 * REQUIRESNEW：总是开启一个新的事务。如果一个事务已经存在，则将这个存在的事务挂起。
 *
 * MANDATORY：如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常。
 *
 * NEVER：总是非事务地执行，如果存在一个活动事务，则抛出异常
 *
 * NESTED：如果一个活动的事务存在，则运行在一个嵌套的事务中。如果没有活动事务，则按REQUIRED属性执行。
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final UserDetailMapper userDetailMapper;

    private final UserAuthMapper userAuthMapper;

    private final UserDao userDao;

    @Autowired
    public LoginServiceImpl(UserDetailMapper userDetailMapper, UserAuthMapper userAuthMapper, UserDao userDao) {
        this.userDetailMapper = userDetailMapper;
        this.userAuthMapper = userAuthMapper;
        this.userDao = userDao;
    }

    @Override
    public Result<LoginResult> login(HttpServletRequest request, HttpServletResponse response) {
        com.qcloud.weapp.authorization.LoginService loginService = new com.qcloud.weapp.authorization.LoginService(request,response);
        UserInfo userInfo = null;
        Result<LoginResult> loginResultResult = null;
        try {
            userInfo = loginService.login();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userInfo!=null){
            boolean flag = save(userInfo);
            if (flag) {
                LoginResult result = new LoginResult(userInfo);
                loginResultResult = makeLoginResult(ResultCode.SUCC,"登录成功",result);
            } else {
                loginResultResult = makeLoginResult(ResultCode.FAIL,"登录失败,请重试",null);
            }
        } else {
            loginResultResult = makeLoginResult(ResultCode.FAIL, "登录失败,请重试",null);
        }
        return loginResultResult;
    }

    @Override
    public Result<PcLoginResult> pcLogin(HttpServletRequest request, HttpServletResponse response, String sid) {
        com.qcloud.weapp.authorization.LoginService loginService = new com.qcloud.weapp.authorization.LoginService(request,response);
        UserInfo userInfo = null;
        try {
            //根据小程序提交的请求验证身份
            userInfo = loginService.check();
        } catch (LoginServiceException | ConfigurationException e) {
            logger.warn("验证身份失败",e.getMessage());
        }
        UserAuth userAuth = null;
        try {
            assert userInfo != null;
            userAuth = userDao.isHaveUserAuthByOpenId(userInfo.getOpenId());
        } catch (Exception e){
            logger.warn("pc登录，查询用户授权失败",e.getMessage());
        }
        //获取userDetails
        UserDetail userDetail = null;
        try {
            assert userAuth != null;
            userDetail = userDetailMapper.selectByPrimaryKey(userAuth.getUserId());
        } catch (Exception e){
            logger.warn("userDetails查询失败",e.getMessage());
        }
        OurUserInfo ourUserInfo = UserInfoMapper.USER_INFO_MAPPER.userDetailToOurUserInfo(userDetail);

        PcLoginResult loginResult = new PcLoginResult();
        //设置用户信息
        loginResult.setUserInfo(ourUserInfo);

        assert userDetail != null;
        //生成登录令牌
        String token = JwtUtils.addAuthentication(userDetail.getNickName(),userDetail.getNickName(),userDetail.getAvatarUrl());
        //设置登录令牌 要求前端保存
        loginResult.setToken(token);

        return new Result<PcLoginResult>(ResultCode.SUCC,loginResult,"登录成功");
    }
    /**
     * 检查Pc token是否合法 合法返回userinfo*/
    @Override
    public Result<OurUserInfo> checkPc(String token) {
        String userId = JwtUtils.getUsernameFromToken(token);
        OurUserInfo userInfo = null;
        //如果token合法
        if (userId!=null) {
            UserDetail userDetail = null;
            try {
                Integer id = Integer.parseInt(userId);
                userDetail = userDetailMapper.selectByPrimaryKey(id);
            } catch (Exception e) {
                logger.info("pc检验权限失败",e.getMessage());
                return new Result<>(ResultCode.FAIL,null,"check fail");
            }
            userInfo = UserInfoMapper.USER_INFO_MAPPER.userDetailToOurUserInfo(userDetail);
        } else {
            return new Result<>(ResultCode.FAIL,null,"check fail");
        }
        return new Result<>(ResultCode.SUCC,userInfo,"check successful");
    }

    @Override
    public Result<OurUserInfo> check(HttpServletRequest request,HttpServletResponse response) {
        com.qcloud.weapp.authorization.LoginService loginService = new com.qcloud.weapp.authorization.LoginService(request,response);
        OurUserInfo ourUserInfo = new OurUserInfo();
        UserInfo userInfo = null;
        try {
            userInfo = loginService.check();
            ourUserInfo = userDao.getOurUserInfoByOpenId(userInfo.getOpenId());
        } catch (LoginServiceException e) {
            logger.info("wx check fail",e);
        } catch (ConfigurationException e) {
            logger.error("qcloud 配置异常",e);
        }
        return new Result<>(ResultCode.SUCC, ourUserInfo, "check successful");
    }

    @Override
    public Result<RefreshToken> refreshToken(String token) {
        String refreshToken = null;
        String userId = JwtUtils.getUsernameFromToken(token);
        Result<RefreshToken> result = new Result<>();
        if (userId!=null) {
            refreshToken = JwtUtils.refreshToken(token);
            RefreshToken r = new RefreshToken();
            r.setToken(refreshToken);
            result.setCode(ResultCode.SUCC);
            result.setMsg("refresh successful");
            result.setData(r);
        } else {
            result.setCode(ResultCode.FAIL);
            result.setMsg("登录失败，请重新登录");
        }
        return result;
    }
    @Cacheable(cacheNames = "UserSimpleInfo",key = "#openId")
    @Override
    public Result<SimpleUserInfo> getUserSimpleInfo(String openId) {
        logger.info("缓存simpleUserinfo,key="+openId);
        Result<SimpleUserInfo> result = new Result<>();
        try {
            SimpleUserInfo simpleUserInfo = userDao.getSimpleUserInfo(openId);
            result.setCode(ResultCode.SUCC);
            result.setData(simpleUserInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ResultCode.FAIL);
            result.setMsg("error");
        }
        return result;
    }

    @Override
    public Result<UserInfo> checkWx(HttpServletRequest request, HttpServletResponse response) {
        com.qcloud.weapp.authorization.LoginService loginService = new com.qcloud.weapp.authorization.LoginService(request,response);
        UserInfo userInfo = null;
        try {
            userInfo = loginService.check();
        } catch (LoginServiceException e) {
            logger.info("wx check fail",e.getMessage());
        } catch (ConfigurationException e) {
            logger.error("qcloud 配置异常",e.getMessage());
        }
        return new Result<>(ResultCode.SUCC, userInfo, "check successful");
    }

    /**
     * 用于保存userInfo信息至数据库
     * */
    @RedisLockable(key = "#userInfo.getOpenId()",expiration = 120)
    @Override
    public boolean save(UserInfo userInfo){
        UserAuth flag = isHaveUser(userInfo.getOpenId());
        //如果是第一次登陆
        if (flag==null) {
            UserDetail userDetail = UserInfoMapper.USER_INFO_MAPPER.userInfoToUserDetail(userInfo);
            userDetail.setCreateTime(getCurrentTime());
            userDetail.setUpdateTime(getCurrentTime());
            try {
                userDetailMapper.insert(userDetail);
            } catch (Exception e){
                logger.warn("插入用户信息失败",e.getMessage());
                return false;
            }

            try {
                UserAuth userAuth = new UserAuth();
                // WX 为表示认证类型为小程序登录 与桌面程序认证分开
                userAuth.setUserId(userDetail.getUserId());
                userAuth.setIdType("WX");
                userAuth.setAuthAccount(userInfo.getOpenId());
                userAuth.setLastLoginTime(getCurrentTime());
                userAuthMapper.insert(userAuth);
            } catch (Exception e) {
                logger.warn("插入用户授权信息失败:",e);
                return false;
            }
        } else {
            //不是第一次登录
            flag.setLastLoginTime(getCurrentTime());
            userAuthMapper.updateByPrimaryKeySelective(flag);
        }
        return true;
    }
    /**根据weixin openid判断是否是第一次登录**/
    private  UserAuth isHaveUser(String wxopenId) {
        UserAuth flag = null;
        try {
            flag = userDao.isHaveUserAuthByOpenId(wxopenId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 生成登录结果*/
    private Result<LoginResult> makeLoginResult(int code, String msg, LoginResult loginResult){
        Result<LoginResult> loginResultResult = new Result<>();
        loginResultResult.setCode(code);
        loginResultResult.setData(loginResult);
        loginResultResult.setMsg(msg);
        return loginResultResult;
    }
    /**
     * 获取当前时间*/
    private Date getCurrentTime(){
        return new Date(System.currentTimeMillis());
    }
}