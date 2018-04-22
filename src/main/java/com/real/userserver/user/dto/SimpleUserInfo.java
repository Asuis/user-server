package com.real.userserver.user.dto;

import lombok.Data;

/**
 * @author asuis
 */
@Data
public class SimpleUserInfo {
    private String nikeName;
    private Integer userId;
    private String avatarUrl;
}
