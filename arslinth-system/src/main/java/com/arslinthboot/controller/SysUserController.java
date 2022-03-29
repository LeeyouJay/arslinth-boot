package com.arslinthboot.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.druid.util.StringUtils;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.common.LoginBody;
import com.arslinthboot.config.redis.RedisTool;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.config.tokenConfig.TokenService;
import com.arslinthboot.entity.SysLogin;
import com.arslinthboot.entity.SysRole;
import com.arslinthboot.entity.SysUser;
import com.arslinthboot.entity.VO.QueryBody;
import com.arslinthboot.service.*;
import com.arslinthboot.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.arslinthboot.common.Constants.*;
import static com.arslinthboot.common.ResponseCode.*;


/**
 * @author Arslinth
 * @ClassName SysUserController
 * @Description 用户相关控制类
 * @Date 2021/7/25
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserController {

    private final RedisTool redisTool;

    private final TokenService tokenService;

    private final SysUserService sysUserService;

    private final UploadService uploadService;

    private final SysLoginService sysLoginService;


    @PostMapping("/login")
    public ApiResponse login(HttpServletRequest request, @RequestBody LoginBody loginBody) {

        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        String moveX = loginBody.getMoveX();
        String captchaUUid = loginBody.getCaptchaUUid();
        SysLogin sysLogin = SysLogin.builder().username(username).build();

        SysUser user = sysUserService.findByUsername(loginBody.getUsername());

        if (user == null) {
            sysLogin.setState(false);
            sysLogin.setMessage("账号不存在！");
            sysLoginService.saveLogin(request, sysLogin);
            return ApiResponse.code(FAIL).message("账号不存在！");
        }

        if (user.getForbidden()) {
            sysLogin.setState(false);
            sysLogin.setMessage("账号已被禁用！");
            sysLoginService.saveLogin(request, sysLogin);
            return ApiResponse.code(FAIL).message("账号已被禁用！");
        }

        if (StringUtils.isEmpty(moveX)) {
            return ApiResponse.code(FAIL).message("验证的值不能为空！");
        }

        if (!redisTool.exists(SLIDER_PREFIX + captchaUUid)) {
            return ApiResponse.code(FAIL).message("验证已过期！");
        }
        //验证拼图
        Integer value = redisTool.getCacheObject(SLIDER_PREFIX + captchaUUid);
        int X = Integer.parseInt(moveX);
        if (!((X < (value + MOVE_CHECK_ERROR))
                && (X > (value - MOVE_CHECK_ERROR)))) {
            return ApiResponse.code(VERIFY_FAIL).message("验证失败！");
        }
        redisTool.deleteObject(SLIDER_PREFIX + captchaUUid);

        int loginTimes = (Integer) Optional.ofNullable(redisTool.getCacheObject(LOGIN_TIMES + username)).orElse(0);
        //判断密码
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            redisTool.setCacheObject(LOGIN_TIMES + username, loginTimes + 1, 10, TimeUnit.MINUTES);
            sysLogin.setState(false);
            sysLogin.setMessage("密码不正确！");
            sysLoginService.saveLogin(request, sysLogin);
            return ApiResponse.code(FAIL).message("密码不正确,还有" + (4 - loginTimes) + "次输入机会");
        }

        if (loginTimes >= 5) {
            sysLogin.setState(false);
            sysLogin.setMessage("密码错误次数过多！");
            sysLoginService.saveLogin(request, sysLogin);
            return ApiResponse.code(FAIL).message("密码错误次数过多，请稍候尝试！");
        }
        redisTool.deleteObject(LOGIN_TIMES + username);

        Set<String> auths = user.getPermissions();
        //初始化登入信息
        LoginUser<SysUser> loginUser =
                SecurityUtils.initLoginUser(user, user.getId(), "sysUser", auths,null);

        //生成token返回前台
        String jwtToken = tokenService.createJwtToken(loginUser);

        sysLoginService.saveLogin(request, sysLogin);
        return ApiResponse.code(SUCCESS).message("登入成功").data("token", jwtToken)
                .data("username", username);
    }

    @GetMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        LoginUser<?> loginUser = SecurityUtils.getLoginUser();
        // TODO 记录日志
        if (loginUser == null) {
            return ApiResponse.code(FAIL).message("注销失败，用户未登入");
        }
        String userKey = LOGIN_TOKEN_KEY + loginUser.getToken();
        redisTool.deleteObject(userKey);
        return ApiResponse.code(SUCCESS).message("注销成功");
    }


    @PostMapping("/list")
    @PreAuthorize("hasAnyAuthority('user_list')")
    public ApiResponse userList(@RequestBody QueryBody query) {
        //TODO
        List<SysUser> userList = sysUserService.getUserList(query);
        return ApiResponse.code(SUCCESS)
                .data("list", userList)
                .data("pageTotal", userList.size());
    }


    //重置密码
    @PostMapping("/resetPassword")
    @PreAuthorize("hasAnyAuthority('resetPassword')")
    public ApiResponse resetPassword(@RequestBody SysUser sysUser) {
        //TODO
        int i = sysUserService.resetPassword(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("重置成功！");
        } else {
            return ApiResponse.code(FAIL).message("密码重置失败！");
        }
    }

    @PostMapping("/setState")
    @PreAuthorize("hasAnyAuthority('changState')")
    public ApiResponse setState(@RequestBody SysUser sysUser) {
        int i = sysUserService.setState(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("更改成功！");
        } else {
            return ApiResponse.code(FAIL).message("更改失败！");
        }
    }


    @GetMapping("/changePassword")
    public ApiResponse changePassword() {

        return ApiResponse.code(FAIL).message("更改失败！");

    }

    @GetMapping("/getUserInfo")
    @PreAuthorize("hasAnyAuthority('dashboard')")
    public ApiResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SysUser user = sysUserService.findByName(username);

        return ApiResponse.code(SUCCESS).message("数据请求成功！").data("userInfo", user);
    }

    @PostMapping("/changeUserInfo")
    @PreAuthorize("hasAnyAuthority('dashboard')")
    public ApiResponse changeUserInfo(@RequestBody SysUser sysUser) {

        int i = sysUserService.changeUserInfo(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("更改成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("手机号或邮箱已被绑定！");
        } else {
            return ApiResponse.code(FAIL).message("更改失败！");
        }
    }

    @PostMapping("/changeAvatar")
    @PreAuthorize("hasAnyAuthority('dashboard')")
    public ApiResponse changeAvatar(@RequestParam("avatarfile") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SysUser user = sysUserService.findByName(username);
        try {
            String imgUrl = uploadService.uploadImg(file, username);
            user.setAvatar(imgUrl);
            int i = sysUserService.changeUserInfo(user);
            if (i == 1) {
                return ApiResponse.code(SUCCESS).message("更改成功！").data("imgUrl", imgUrl);
            } else if (i == -1) {
                return ApiResponse.code(FAIL).message("手机号或邮箱已被绑定！");
            } else {
                return ApiResponse.code(FAIL).message("更改失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.code(FAIL).message("更改失败！");
        }
    }

}
