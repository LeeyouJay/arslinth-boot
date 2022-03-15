package com.archiveboot.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.archiveboot.dao.SysUserDao;
import com.archiveboot.entity.SysUser;
import com.archiveboot.entity.VO.QueryBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.archiveboot.common.Constants.RESET_CODE;


/**
 * @author Arslinth
 * @ClassName SysUserService
 * @Description TODO
 * @Date 2021/7/25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserService {

    private final SysUserDao sysUserDao;

    public SysUser findByUsername(String username) {
        return sysUserDao.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    public List<SysUser> getUserList(QueryBody query) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        if (!StringUtils.isEmpty(query.getState())) {
            wrapper.eq("state", query.getState());
        }
        if (!StringUtils.isEmpty(query.getSearchName())) {
            wrapper.and(w -> w.like("nick_name", query.getSearchName())
                    .or().like("username", query.getSearchName())
                    .or().like("phone", query.getSearchName()));
        }

        List<SysUser> sysUsers = sysUserDao.selectList(wrapper);
        sysUsers.forEach(u -> {
            u.setPassword("");//把密码设为空
        });
        return sysUsers;
    }

    public SysUser findByName(String username) {
        return sysUserDao.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    public int resetPassword(SysUser sysUser) {
        sysUser.setPassword(new BCryptPasswordEncoder().encode(RESET_CODE));
        return sysUserDao.updateById(sysUser);
    }

    public int setState(SysUser sysUser) {
        return sysUserDao.setState(sysUser);
    }

    public int changePassword(SysUser sysUser) {
        return sysUserDao.updateById(sysUser);
    }

    public int changeUserInfo(SysUser sysUser) {
        return sysUserDao.changeUserInfo(sysUser);
    }

    public void setRight(String username) {
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<SysUser>().eq("username", username).set("set_right", true);
        sysUserDao.update(null, wrapper);
    }

}
