package com.arslinthboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.config.redis.RedisTool;
import com.arslinthboot.config.tokenConfig.TokenProperty;
import com.arslinthboot.utils.HttpServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.arslinthboot.common.Constants.REPEAT_SUBMIT_KEY;


/**
 * @className: InterceptorService
 * @description: 判断url请求表单是否重复提交
 * @author: Arslinth
 * @date: 2022/2/23
 **/
@Service
@EnableConfigurationProperties(TokenProperty.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RepeatInterceptorService {

    private final RedisTool redisTool;

    private final TokenProperty tokenProperty;

    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper) {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpServletUtil.getBodyString(repeatedlyRequest);
        }

        // body参数为空，获取Parameter的数据
        if (StrUtil.isEmpty(nowParams)) {
            nowParams = JSONObject.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put("repeatParams", nowParams);
        nowDataMap.put("repeatTime", System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

        // 唯一值（没有消息头则使用请求地址）
        String authorization = request.getHeader(tokenProperty.getHeader());

        String submitKey = StrUtil.trimToEmpty(authorization);

        // 唯一标识（指定key + url + empId）annotation
        String cacheRepeatKey = REPEAT_SUBMIT_KEY + url + submitKey;


        Object sessionObj = redisTool.getCacheObject(cacheRepeatKey);
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval())) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        redisTool.setCacheObject(cacheRepeatKey, cacheMap, annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, long interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1 - time2) < interval) {
            return true;
        }
        return false;
    }
}
