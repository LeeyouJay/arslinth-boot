package com.arslinthboot.controller;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.redis.RedisTool;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.entity.BaseEntity;
import com.arslinthboot.entity.SysServer;
import com.arslinthboot.utils.PageDomain;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.arslinthboot.common.Constants.LOGIN_TOKEN_KEY;
import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @author Lujb
 * @ClassName SysServerController
 * @Description 系统信息
 * @Date 2021/7/27
 */
@Slf4j
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysServerController {

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisTool redisTool;

    /**
     * 获取系统信息
     */
    @GetMapping("/getServerInfo")
    public ApiResponse getServerInfo() throws Exception {
        SysServer server = new SysServer();
        server.copyTo();
        return ApiResponse.code(SUCCESS).data("server", server).message("读取成功");
    }

    /**
     * 获取缓存信息
     */
    @GetMapping("/getCacheInfo")
    public ApiResponse getCacheInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);

            data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
            data.put("value", StrUtil.subBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        return ApiResponse.code(SUCCESS)
                .data("info", info)
                .data("dbSize", dbSize)
                .data("commandStats", pieList)
                .message("读取成功");
    }


    /**
     * 获取在线用户
     */
    @PostMapping("/onlineUserPage")
    public ApiResponse getOnlineUserPage(@RequestBody LoginUser<?> queryBody) {
        Page<BaseEntity> page = PageDomain.buildPage();
        long pageIndex = page.getCurrent();
        long pageSize = page.getSize();

        Set<String> tokens = redisTool.scanKeys(LOGIN_TOKEN_KEY);
        List<LoginUser<?>> list = new ArrayList<>();
        for (String token : tokens) {
            LoginUser<?> loginUser = redisTool.getCacheObject(token);
            LoginUser<?> build = LoginUser.builder().token(loginUser.getToken())
                    .username(loginUser.getUsername())
                    .ipaddr(loginUser.getIpaddr())
                    .browser(loginUser.getBrowser())
                    .loginLocation(loginUser.getLoginLocation())
                    .loginTime(loginUser.getLoginTime())
                    .os(loginUser.getOs())
                    .userType(loginUser.getUserType()).build();
            list.add(build);
        }
        String location = Optional.ofNullable(queryBody.getLoginLocation()).orElse("");
        String username = Optional.ofNullable(queryBody.getUsername()).orElse("");
        List<LoginUser<?>> users = list.stream().filter(f1 -> StrUtil.contains(f1.getLoginLocation(), location))
                .filter(f2 -> StrUtil.contains(f2.getIpaddr(), username)).sorted(Comparator.comparingLong(LoginUser::getLoginTime))
                .skip((pageIndex - 1) * pageSize).limit(pageSize).collect(Collectors.toList());

        return ApiResponse.code(SUCCESS)
                .data("list", users)
                .data("page", pageIndex)
                .data("total", list.size())
                .message("查询成功！");
    }

    /**
     * 强退用户
     */
    @RepeatSubmit
    @GetMapping("/forceLogout/{tokenId}")
    @PreAuthorize("@auth.hasAnyAuthority('ForceLogout')")
    public ApiResponse forceLogout(@PathVariable String tokenId) {
        redisTool.deleteObject(LOGIN_TOKEN_KEY + tokenId);
        return ApiResponse.code(SUCCESS).message("强退成功！");
    }

}
