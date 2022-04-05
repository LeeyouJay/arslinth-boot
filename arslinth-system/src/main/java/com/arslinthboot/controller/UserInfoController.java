package com.arslinthboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.entity.SysUser;
import com.arslinthboot.service.SysUserService;
import com.arslinthboot.service.UploadService;
import com.arslinthboot.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @author Arslinth
 * @ClassName UserInfoController
 * @Description 用户信息
 * @Date 2022/4/4
 */
@Slf4j
@RestController
@RequestMapping("/userinfo")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoController {

    private final SysUserService sysUserService;

    private final UploadService uploadService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/getUserInfo")
    public ApiResponse getUserInfo() {
        String userId = SecurityUtils.getUserId();
        SysUser user = sysUserService.getUserById(userId);
        SysUser build = SysUser.builder()
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .phone(user.getPhone())
                .sex(user.getSex())
                .email(user.getEmail())
                .permissions(user.getPermissions())
                .createTime(user.getCreateTime()).build();
        return ApiResponse.code(SUCCESS).data("user", build).message("数据请求成功！");
    }


    /**
     * 当前用户更改信息
     */
    @RepeatSubmit
    @PostMapping("/changeUserInfo")
    public ApiResponse changeUserInfo(@RequestBody SysUser sysUser) {
        String userId = SecurityUtils.getUserId();
        sysUser.setId(userId);
        int i = sysUserService.changeUserInfo(sysUser);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("更改成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("手机号或邮箱已被绑定！");
        } else {
            return ApiResponse.code(FAIL).message("更改失败！");
        }
    }

    /**
     * 修改头像
     */
    @RepeatSubmit
    @PostMapping("/changeAvatar")
    public ApiResponse changeAvatar(@RequestParam("avatarFile") MultipartFile file) {
        SysUser user = SecurityUtils.getUser(SysUser.class);
        if (user == null) {
            return ApiResponse.code(FAIL).message("用户未登入");
        }
        try {
            String url = uploadService.uploadImg(file, user.getUsername());
            sysUserService.editUser(SysUser.builder().id(user.getId()).avatar(url).build());
            return ApiResponse.code(SUCCESS).data("url", url).message("修改成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.code(FAIL).message("更改失败！");
        }


    }

    /**
     * 修改密码
     */
    @RepeatSubmit
    @PostMapping("/changePassword")
    public ApiResponse changePassword(@RequestBody JSONObject jsonObject) {
        String userId = SecurityUtils.getUserId();
        String oldPass = Optional.ofNullable(jsonObject.get("oldPass")).map(Object::toString).orElse("");
        String newPass = Optional.ofNullable(jsonObject.get("newPass")).map(Object::toString).orElse("");
        int i = sysUserService.changePassword(userId, oldPass, newPass);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("更改成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("用户不存在！");
        } else if (i == -2) {
            return ApiResponse.code(FAIL).message("密码不正确！");
        } else {
            return ApiResponse.code(FAIL).message("更新异常：" + i);
        }

    }
}
