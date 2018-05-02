package com.real.userserver.user.dto;

/**
 * @author asuis
 */
public class PcLoginResult {
    private OurUserInfo userInfo;
    private String token;
    private String refreshToken;

    public PcLoginResult() {
    }

    public OurUserInfo getUserInfo() {
        return this.userInfo;
    }

    public String getToken() {
        return this.token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setUserInfo(OurUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PcLoginResult)) return false;
        final PcLoginResult other = (PcLoginResult) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userInfo = this.getUserInfo();
        final Object other$userInfo = other.getUserInfo();
        if (this$userInfo == null ? other$userInfo != null : !this$userInfo.equals(other$userInfo)) return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
        final Object this$refreshToken = this.getRefreshToken();
        final Object other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userInfo = this.getUserInfo();
        result = result * PRIME + ($userInfo == null ? 43 : $userInfo.hashCode());
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 43 : $token.hashCode());
        final Object $refreshToken = this.getRefreshToken();
        result = result * PRIME + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PcLoginResult;
    }

    public String toString() {
        return "PcLoginResult(userInfo=" + this.getUserInfo() + ", token=" + this.getToken() + ", refreshToken=" + this.getRefreshToken() + ")";
    }
}
