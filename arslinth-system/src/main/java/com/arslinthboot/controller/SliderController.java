package com.arslinthboot.controller;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.common.ImageResult;
import com.arslinthboot.config.redis.RedisTool;
import com.arslinthboot.utils.SliderImgUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.arslinthboot.common.Constants.SLIDER_PREFIX;
import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;


@Slf4j
@RestController
@RequestMapping("/slider")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SliderController {

    private int xPosCache = 0;

    private final RedisTool redisTool;

    @GetMapping("/image")
    public ApiResponse image() {
        log.info("/slider/image");
        ImageResult imageResult = null;

        try {

            imageResult = new SliderImgUtil().imageResult();

            xPosCache = imageResult.getXpos();
            String captchaUUid = IdUtil.simpleUUID();
            redisTool.setCacheObject(SLIDER_PREFIX + captchaUUid, xPosCache, 5, TimeUnit.MINUTES);
            imageResult.setXpos(0);

            Map<String, Object> img = JSON.parseObject(JSON.toJSONString(imageResult));
            img.put("captchaUUid", captchaUUid);
            return ApiResponse.code(SUCCESS).data(img).message("图片请求成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ApiResponse.code(FAIL).message("图片请求失败！");
        }
    }


    @GetMapping("/verification")
    public ApiResponse verification(@RequestParam("moveX") int moveX) {

        log.info("/slider/verification/{}", moveX);

        int MOVE_CHECK_ERROR = 2;
        if ((moveX < (xPosCache + MOVE_CHECK_ERROR))
                && (moveX > (xPosCache - MOVE_CHECK_ERROR))) {
            log.info("验证正确");
            return ApiResponse.code(SUCCESS).data("verify", true);
        }
        return ApiResponse.code(FAIL).data("verify", false);
    }


}
