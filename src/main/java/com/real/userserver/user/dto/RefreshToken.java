package com.real.userserver.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author asuis
 */
@Setter
@Getter
public class RefreshToken {
    private String token;
    private String refreshToken;
}
