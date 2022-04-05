package com.arslinthboot.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.SysRoleDao;
import com.arslinthboot.entity.SysRole;
import com.arslinthboot.utils.PageDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.arslinthboot.dao.SysUserDao;
import com.arslinthboot.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.arslinthboot.common.Constants.RESET_CODE;


/**
 * @author Arslinth
 * @ClassName SysUserService
 * @Description
 * @Date 2021/7/25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserService {

    private final SysUserDao sysUserDao;

    private final SysRoleDao sysRoleDao;

    public SysUser findByUsername(String username) {
        return sysUserDao.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    public Page<SysUser> getUserPage(SysUser sysUser) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        Page<SysUser> page = PageDomain.buildPage();
        wrapper.orderByAsc("index_num");
        if (StrUtil.isNotEmpty(sysUser.getNickName())) {
            wrapper.and(w -> w.like("nick_name", sysUser.getNickName())
                    .or().like("username", sysUser.getNickName())
                    .or().like("phone", sysUser.getNickName()));
        }
        if (sysUser.getForbidden() != null) {
            wrapper.eq("forbidden", sysUser.getForbidden());
        }

        wrapper.select(SysUser.class,
                u -> !"password".equals(u.getColumn()));
        return sysUserDao.selectPage(page, wrapper);
    }

    public SysUser getUserById(String id) {
        SysUser sysUser = sysUserDao.selectById(id);
        sysUser.setPassword(null);
        return sysUser;
    }

    public void addUser(SysUser sysUser) {
        setRolePermissions(sysUser);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(RESET_CODE));
        sysUserDao.insert(sysUser);
    }

    public int editUser(SysUser sysUser) {
        return sysUserDao.updateById(sysUser);
    }

    public int setPermissions(SysUser sysUser) {
        setRolePermissions(sysUser);
        return sysUserDao.updateById(sysUser);
    }

    public int delById(String id) {
        if ("1".equals(id)) {
            return -1;
        }
        return sysUserDao.deleteById(id);
    }


    public int resetPassword(SysUser sysUser) {
        sysUser.setPassword(new BCryptPasswordEncoder().encode(RESET_CODE));
        return sysUserDao.updateById(sysUser);
    }

    public int changePassword(String userId, String oldPass, String newPass) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            return -1;
        }
        if (!new BCryptPasswordEncoder().matches(oldPass, user.getPassword())) {
            return -2;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(newPass));
        return sysUserDao.updateById(user);
    }


    public int changeUserInfo(SysUser sysUser) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        String email = sysUser.getEmail();
        String phone = sysUser.getPhone();
        wrapper.ne("id", sysUser.getId()).and(w -> w.ne("phone", phone).or().eq("email", email));
        List<SysUser> users = sysUserDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(users)) {
            return -1;
        }
        return sysUserDao.updateById(sysUser);
    }


    public int delByIds(List<String> ids) {
        if (ids.contains("1")) {
            return -1;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return sysUserDao.delete(wrapper);
    }

    private void setRolePermissions(SysUser sysUser) {
        sysUser.setPermissions(new HashSet<>());
        Set<String> roleIds = sysUser.getRoleIds();
        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysRole> roles = sysRoleDao.selectList(new QueryWrapper<SysRole>().in("id", roleIds));
            List<Set<String>> collect = roles.stream().map(SysRole::getPermissions).collect(Collectors.toList());
            Set<String> permissions = new HashSet<>();
            collect.forEach(permissions::addAll);
            sysUser.setStrictly(false);
            sysUser.setPermissions(permissions);
        }
    }
}
