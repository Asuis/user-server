package com.real.userserver.user.dto;

import com.qcloud.weapp.authorization.UserInfo;

/**
 * @author asuis
 */
public class LoginResult {
    private UserInfo userInfo;

    public LoginResult(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
