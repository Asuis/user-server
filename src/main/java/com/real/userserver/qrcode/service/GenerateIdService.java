package com.real.userserver.qrcode.service;

/**
 * @author asuis
 * pc登录时 生成临时唯一id
 */
public interface GenerateIdService {
    /**
     * 用与pc端二维码扫描登录
     * @return 生成短时间内唯一id
     * */
    public Long generateId();
}
