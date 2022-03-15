package com.archiveboot.config.tokenConfig;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.archiveboot.config.redis.RedisTool;
import com.archiveboot.exception.IllegalTokenException;
import com.archiveboot.utils.HttpServletUtil;
import com.archiveboot.utils.IpInfoUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.archiveboot.common.Constants.LOGIN_TOKEN_KEY;
import static com.archiveboot.common.Constants.LOGIN_USER_KEY;

/**
 * @className: TokenService
 * @description: token 验证处理类
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@Slf4j
@Component
@EnableConfigurationProperties(TokenProperty.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenService {

    private final TokenProperty tokenProperty;

    private final RedisTool redisTool;


    //生成jwt
    public String createJwtToken(LoginUser<?> loginUser) {
        String token = IdUtil.randomUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);
        Map<String, Object> map = new HashMap<>();
        map.put(LOGIN_USER_KEY, token);
        return JWTUtil.createToken(map, tokenProperty.getSecret().getBytes());
    }

    public LoginUser<?> checkToken(String header) throws IllegalTokenException {

        try {
            boolean verify = JWTUtil.verify(header, tokenProperty.getSecret().getBytes());
            if (!verify) {
                throw new IllegalTokenException("token验证不通过！");
            }
            JWT jwt = JWTUtil.parseToken(header);
            String token = Optional.ofNullable(jwt.getPayload(LOGIN_USER_KEY))
                    .map(Object::toString).orElse("");
            String userKey = LOGIN_TOKEN_KEY + token;
            LoginUser<?> loginUser = redisTool.getCacheObject(userKey);
            if (loginUser == null) {
                throw new IllegalTokenException("登入信息已失效，请重新登入！");
            }
            LocalDateTime expireValue = LocalDateTimeUtil.of(loginUser.getExpireTime());
            long minutes = LocalDateTimeUtil.between(LocalDateTime.now(), expireValue).toMinutes();
            if (minutes < 0) {
                throw new IllegalTokenException("登入信息已失效，请重新登入！");
            }
            if (minutes < tokenProperty.getInterval()) {
                refreshToken(loginUser);
            }
            return loginUser;

        } catch (IllegalTokenException e) {
            throw e;
        } catch (Exception e) {
            log.info("token解析异常：{}", e.getMessage());
            throw new IllegalTokenException("token解析异常！");
        }
    }


    private void setUserAgent(LoginUser<?> loginUser) {
        HttpServletRequest request = HttpServletUtil.getRequest();
        String ipAddr = IpInfoUtil.getIpAddr(request);
        String loginLocation;
        try {
            loginLocation = IpInfoUtil.getipSource(ipAddr);
        } catch (Exception e) {
            log.info("获取ip位置失败：{}", e.getMessage());
            loginLocation = "未知";
        }
        loginUser.setIpaddr(ipAddr);
        loginUser.setOs(IpInfoUtil.getSystemName(request));
        loginUser.setBrowser(IpInfoUtil.getBrowser(request));
        loginUser.setLoginLocation(loginLocation);
    }

    private void refreshToken(LoginUser<?> loginUser) {
        long currentTimeMillis = System.currentTimeMillis();
        loginUser.setLoginTime(currentTimeMillis);
        loginUser.setExpireTime(currentTimeMillis + tokenProperty.getExpireTime() * 60 * 1000L);
        String userKey = LOGIN_TOKEN_KEY + loginUser.getToken();
        redisTool.setCacheObject(userKey, loginUser, tokenProperty.getExpireTime(), TimeUnit.MINUTES);
    }

}
