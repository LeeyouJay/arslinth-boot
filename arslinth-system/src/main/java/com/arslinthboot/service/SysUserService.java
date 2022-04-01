package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.utils.PageDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.arslinthboot.dao.SysUserDao;
import com.arslinthboot.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public SysUser findByUsername(String username) {
        return sysUserDao.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    public Page<SysUser> getUserPage(SysUser sysUser) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        Page<SysUser> page = PageDomain.buildPage();
        wrapper.orderByDesc("create_time");
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
        sysUser.setPassword(new BCryptPasswordEncoder().encode(RESET_CODE));
        sysUserDao.insert(sysUser);
    }

    public int editUser(SysUser sysUser) {
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


    public int changeUserInfo(SysUser sysUser) {
        return sysUserDao.changeUserInfo(sysUser);
    }


    public int delByIds(List<String> ids) {
        if (ids.contains("1")) {
            return -1;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return sysUserDao.delete(wrapper);
    }
}
