package com.real.userserver.user;

import com.real.userserver.dto.Result;
import com.real.userserver.user.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author asuis
 */
public interface LoginService {
    /**
     *登录
     * @param request 登录请求
     * @param response 登录响应
     * @return login 登录结果
     **/
    public Result<LoginResult> login(HttpServletRequest request, HttpServletResponse response);
    /**
     * pc端登录
     * @param request 登录请求
     * @param response 登录响应
     * @param sid 通过扫描二维码得到的sid
     * @return login 登录结果
     * */
    public Result<PcLoginResult> pcLogin(HttpServletRequest request, HttpServletResponse response, String sid);
    /**
     * 根据request检查
     * */
    public Result<OurUserInfo> checkPc(String token);

    public Result<OurUserInfo> check(HttpServletRequest request,HttpServletResponse response);
    /**
     * 更新pc用户登录token
     * */
    public Result<RefreshToken> refreshToken(String token);

    public Result<SimpleUserInfo> getUserSimpleInfo(String openId);
}
