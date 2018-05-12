package com.real.userserver.user;

import com.qcloud.weapp.authorization.UserInfo;
import com.real.userserver.dto.Result;
import com.real.userserver.user.dto.OurUserInfo;
import com.real.userserver.user.dto.PcLoginResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author asuis
 */
@RestController
@Api(description = "登录Api")
public class LoginController {

    private final com.real.userserver.user.LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "/wx/login",method = RequestMethod.GET)
    @ApiOperation(value = "微信小程序登录",notes = "此接口直接使用微信提供的sdk即可调研，下面的参数均在header中获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-WX-Code",paramType = "header",value = "header中获取通过小程序api:wx.login()获取code（时效5分钟）,使用wx.getUserInfo(),还会同时提交"),
            @ApiImplicitParam(name = "X-WX-Encrypted-Data",paramType = "header"),
            @ApiImplicitParam(name = "X-WX-IV",paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "登录成功")
    })
    public void wxLogin(HttpServletRequest request, HttpServletResponse response){
        loginService.login(request,response);
    }

    @RequestMapping(value = "/pc/login",method = RequestMethod.POST)
    @ApiOperation(value = "PC展示端登录",notes = "此接口用于使用微信二维码登录pc端的情形")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid",value = "服务端生成的二维码中的唯一sid（5分钟内）"),
            @ApiImplicitParam(name = "token",paramType = "header",value = "微信小程序登录时客户端保存的令牌,这里使用sdk带会话请求，同时传递sid,服务端中途会进行login.check(),通过则登录成功")
    })
    public Result<PcLoginResult> pcLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam("sid") String sid){
        return loginService.pcLogin(request,response,sid);
    }

    @RequestMapping(value = "/pc/check",method = RequestMethod.GET)
    @ApiOperation(value = "pc登录检查")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "Authorization", value = "权限认证用Header", type = "header")
    )
    public Result<OurUserInfo> pcLoginCheck(@RequestHeader("Authorization")String token){
        return loginService.checkPc(token);
    }

    @RequestMapping(value = "/wx/check",method = RequestMethod.GET)
    @ApiOperation(value = "微信小程序登录检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "WX_HEADER_ID",value = "wafer2登录获得的id",type = "header"),
            @ApiImplicitParam(name = "WX_HEADER_SKEY",value = "wafer2登录获得的SKEY",type = "header")
    })
    public Result<OurUserInfo> wxLoginCheck(HttpServletRequest request,HttpServletResponse response){
        return loginService.check(request,response);
    }

    @RequestMapping(value = "/wx/user",method = RequestMethod.GET)
    @ApiOperation(value = "微信小程序登录检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "WX_HEADER_ID",value = "wafer2登录获得的id",type = "header"),
            @ApiImplicitParam(name = "WX_HEADER_SKEY",value = "wafer2登录获得的SKEY",type = "header")
    })
    public Result<UserInfo> wxUser(HttpServletRequest request, HttpServletResponse response){
        return loginService.checkWx(request,response);
    }

}