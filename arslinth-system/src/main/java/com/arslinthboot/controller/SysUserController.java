package com.arslinthboot.controller;

import com.alibaba.druid.util.StringUtils;
import com.arslinthboot.annotation.SysLog;
import com.arslinthboot.annotation.RepeatSubmit;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.arslinthboot.common.Constants.*;
import static com.arslinthboot.common.ResponseCode.*;


/**
 * @author Arslinth
 * @ClassName SysUserController
 * @Description 系统用户
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
                SecurityUtils.initLoginUser(user, user.getId(), user.getUsername(), SYSTEM_USER, auths, null);

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
            return ApiResponse.code(FAIL).message("用户登入状态已过期！");
        }
        LoginLog loginLog = LoginLog.builder()
                .username(loginUser.getUser().getUsername())
                .state(true)
                .userType(loginUser.getUserType())
                .message("注销成功")
                .build();

        sysLogService.saveLoginLog(request, loginLog);
        String userKey = LOGIN_TOKEN_KEY + loginUser.getToken();
        redisTool.deleteObject(userKey);
        return ApiResponse.code(SUCCESS).message("注销成功");
    }

    /**
     * 获取用户列表
     */
    @PostMapping("/userPage")
    @PreAuthorize("@auth.hasAnyAuthority('SysUser')")
    public ApiResponse userPage(@RequestBody SysUser sysUser) {
        Page<SysUser> userPage = sysUserService.getUserPage(sysUser);
        return ApiResponse.code(SUCCESS)
                .data("list", userPage.getRecords())
                .data("page", userPage.getPages())
                .data("total", userPage.getTotal())
                .message("查询成功！");
    }

    /**
     * 添加用户
     */
    @SysLog("#{'添加用户:'+#sysUser.username}")
    @RepeatSubmit
    @PostMapping("/add")
    @PreAuthorize("@auth.hasAnyAuthority('AddUser')")
    public ApiResponse addUser(@RequestBody SysUser sysUser) {
        sysUserService.addUser(sysUser);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    /**
     * 根据id查询用户
     */
    @GetMapping("/getUserById/{id}")
    @PreAuthorize("@auth.hasAnyAuthority('SysUser')")
    public ApiResponse getUserById(@PathVariable("id") String id) {
        return ApiResponse.code(SUCCESS).data("user", sysUserService.getUserById(id));
    }

    /**
     * 修改用户信息
     */
    @SysLog("#{'修改用户'+#sysUser.username+'信息'}")
    @RepeatSubmit
    @PostMapping("/edit")
    @PreAuthorize("@auth.hasAnyAuthority('EditUser')")
    public ApiResponse editUser(@RequestBody SysUser sysUser) {
        int i = sysUserService.setPermissions(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("修改成功！");
        } else {
            return ApiResponse.code(FAIL).message("修改出现异常：" + i);
        }
    }

    /**
     * 设置用户操作权限
     */
    @SysLog("#{'设置'+#sysUser.username+'用户操作权限'}")
    @RepeatSubmit
    @PostMapping("/setPermissions")
    @PreAuthorize("@auth.hasAnyAuthority('SetPermissions')")
    public ApiResponse setPermissions(@RequestBody SysUser sysUser) {
        int i = sysUserService.editUser(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("修改成功！");
        } else {
            return ApiResponse.code(FAIL).message("修改出现异常：" + i);
        }
    }


    /**
     * 删除用户
     */
    @SysLog("#{'删除用户id:'+#id}")
    @RepeatSubmit
    @GetMapping("/del/{id}")
    @PreAuthorize("@auth.hasAnyAuthority('DelUser')")
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


    /**
     * 批量删除用户
     */
    @SysLog("批量删除用户")
    @RepeatSubmit
    @PostMapping("/delUserByIds")
    @PreAuthorize("@auth.hasAnyAuthority('DelUser')")
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

    /**
     * 重置密码
     */
    @SysLog("#{'重置用户'+#sysUser.username+'密码'}")
    @RepeatSubmit
    @PostMapping("/resetPassword")
    @PreAuthorize("@auth.hasAnyAuthority('ResetPassword')")
    public ApiResponse resetPassword(@RequestBody SysUser sysUser) {
        int i = sysUserService.resetPassword(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("重置成功！");
        } else {
            return ApiResponse.code(FAIL).message("密码重置失败！");
        }
    }

}
