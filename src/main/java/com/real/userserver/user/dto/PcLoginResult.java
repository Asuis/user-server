package com.real.userserver.user.dto;

import com.real.userserver.user.model.UserDetail;
import lombok.Data;

/**
 * @author asuis
 */
@Data
public class PcLoginResult {
    private OurUserInfo userInfo;
    private String token;
    private String refreshToken;
}
