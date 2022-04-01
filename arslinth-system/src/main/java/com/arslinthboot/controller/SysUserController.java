package com.arslinthboot.controller;

import com.alibaba.druid.util.StringUtils;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.common.LoginBody;
import com.arslinthboot.config.redis.RedisTool;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.config.tokenConfig.TokenService;
import com.arslinthboot.entity.LoginLog;
import com.arslinthboot.entity.SysUser;
import com.arslinthboot.service.*;
import com.arslinthboot.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private final SysLogService sysLogService;


    @PostMapping("/login")
    public ApiResponse login(HttpServletRequest request, @RequestBody LoginBody loginBody) {

        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        String moveX = loginBody.getMoveX();
        String captchaUUid = loginBody.getCaptchaUUid();

        LoginLog loginLog = LoginLog.builder().username(username).build();

        SysUser user = sysUserService.findByUsername(loginBody.getUsername());

        if (user == null) {
            loginLog.setState(false);
            loginLog.setMessage("账号不存在！");
            sysLogService.saveLoginLog(request, loginLog);
            return ApiResponse.code(FAIL).message("账号不存在！");
        }

        loginLog.setUserType(SYSTEM_USER);

        if (user.getForbidden()) {
            loginLog.setState(false);
            loginLog.setMessage("账号已被禁用！");
            sysLogService.saveLoginLog(request, loginLog);
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
            loginLog.setState(false);
            loginLog.setMessage("密码不正确！");
            sysLogService.saveLoginLog(request, loginLog);
            return ApiResponse.code(FAIL).message("密码不正确,还有" + (4 - loginTimes) + "次输入机会");
        }

        if (loginTimes >= 5) {
            loginLog.setState(false);
            loginLog.setMessage("密码错误次数过多！");
            sysLogService.saveLoginLog(request, loginLog);
            return ApiResponse.code(FAIL).message("密码错误次数过多，请稍候尝试！");
        }
        redisTool.deleteObject(LOGIN_TIMES + username);

        Set<String> auths = user.getPermissions();
        //初始化登入信息
        LoginUser<SysUser> loginUser =
                SecurityUtils.initLoginUser(user, user.getId(), SYSTEM_USER, auths,null);

        //生成token返回前台
        String jwtToken = tokenService.createJwtToken(loginUser);
        loginLog.setState(true);
        loginLog.setMessage("登入成功");
        sysLogService.saveLoginLog(request, loginLog);
        return ApiResponse.code(SUCCESS).message("登入成功").data("token", jwtToken)
                .data("username", username);
    }

    @GetMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        LoginUser<SysUser> loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return ApiResponse.code(FAIL).message("注销失败，用户未登入");
        }
        LoginLog loginLog = LoginLog.builder()
                .username(loginUser.getUser().getUsername())
                .state(true)
                .userType(loginUser.getUserType())
                .message("注销成功")
                .build();

        sysLogService.saveLoginLog(request,loginLog);
        String userKey = LOGIN_TOKEN_KEY + loginUser.getToken();
        redisTool.deleteObject(userKey);
        return ApiResponse.code(SUCCESS).message("注销成功");
    }


    @PostMapping("/userPage")
    public ApiResponse userPage(@RequestBody SysUser sysUser) {
        Page<SysUser> userPage = sysUserService.getUserPage(sysUser);
        return ApiResponse.code(SUCCESS)
                .data("list", userPage.getRecords())
                .data("page", userPage.getPages())
                .data("total", userPage.getTotal())
                .message("查询成功！");
    }

    @PostMapping("/add")
    public ApiResponse addUser(@RequestBody SysUser sysUser) {
        sysUserService.addUser(sysUser);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    @GetMapping("/getUserById/{id}")
    public ApiResponse getDictById(@PathVariable("id") String id) {
        return ApiResponse.code(SUCCESS).data("user", sysUserService.getUserById(id));
    }

    @PostMapping("/edit")
    public ApiResponse editUser(@RequestBody SysUser sysUser) {
        int i = sysUserService.editUser(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("修改成功！");
        } else {
            return ApiResponse.code(FAIL).message("修改出现异常：" + i);
        }
    }

    @GetMapping("/del/{id}")
    public ApiResponse delUser(@PathVariable String id) {
        int i = sysUserService.delById(id);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("系统管理员不能删除！");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常：" + i);
        }
    }

    @PostMapping("/delUserByIds")
    public ApiResponse delUserByIds(@RequestBody List<String> ids) {
        int i = sysUserService.delByIds(ids);
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("系统管理员不能删除！");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常：" + i);
        }
    }

    //重置密码
    @PostMapping("/resetPassword")
    public ApiResponse resetPassword(@RequestBody SysUser sysUser) {
        int i = sysUserService.resetPassword(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("重置成功！");
        } else {
            return ApiResponse.code(FAIL).message("密码重置失败！");
        }
    }

    @GetMapping("/changePassword")
    public ApiResponse changePassword() {

        return ApiResponse.code(FAIL).message("更改失败！");

    }

    @GetMapping("/getUserInfo")
    @PreAuthorize("hasAnyAuthority('dashboard')")
    public ApiResponse getUserInfo() {


        return ApiResponse.code(SUCCESS).message("数据请求成功！");
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

        return ApiResponse.code(FAIL).message("更改失败！");

    }

}
