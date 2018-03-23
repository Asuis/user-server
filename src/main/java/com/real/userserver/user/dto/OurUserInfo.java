package com.real.userserver.user.dto;

import lombok.Data;

/**
 * @author asuis
 */
@Data
public class OurUserInfo {
    private Integer userId;
    private String nickName;
    private String avatarUrl;
    private Integer gender;
    private String language;
    private String city;
    private String province;
    private String country;
}