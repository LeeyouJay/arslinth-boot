package com.arslinthboot.config.tokenConfig;

import cn.hutool.core.collection.CollUtil;
import com.arslinthboot.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @className: PermissionService
 * @description: 自定义权限控制
 * @author: Arslinth
 * @date: 2022/4/2
 **/
@Service("auth")
public class PermissionService {

    public boolean hasAnyAuthority(String... authorities) {
        Set<String> auths = CollUtil.newHashSet(authorities);
        if (CollUtil.isEmpty(auths)) {
            return false;
        }

        LoginUser<?> loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollUtil.isEmpty(loginUser.getPermissions())) {
            return false;
        }

        Set<String> permissions = loginUser.getPermissions();
        if (permissions.contains("*")) {
            return true;
        }

        for (String auth : auths) {
            if (permissions.contains(auth)){
                return true;
            }
        }
        return false;
    }
}
