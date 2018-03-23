package com.real.userserver.qrcode;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.real.userserver.qrcode.service.GenerateIdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author asuis
 */
@Controller
@RequestMapping("/v1/qr")
@Api(description = "pc端二维码生成")
public class QRcodeController {
    @Autowired
    private GenerateIdService generateIdService;
    /**
     * 生成二维码
     * @param response
     * @param size
     * @param format
     * @param content
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping(value = "make", method = RequestMethod.GET)
    @ApiOperation("生成二维码用作pc登录")
    public void readZxing(HttpServletResponse response, Integer size, Integer margin, String level,
                          String format, String content) throws WriterException, IOException {
        Integer width,height;
        Long sid = generateIdService.generateId();
        if(size == null){
            width = 200;
            height = 200;
        }else{
            width = size;
            height = size;
        }
        if(margin == null) {
            margin = 0;
        }
        if(level == null) {
            level = "L";
        }
        if(format == null) {
            format = "gif";
        }
        if(content == null) {
            content = "http://tool.yoodb.com/qrcode/generate"+sid;
        }
        ZxingUtil.createZxing(response, width, height, margin, level, format, content);
    }
}