package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.SysRoleDao;
import com.arslinthboot.entity.SysRole;
import com.arslinthboot.utils.SelectDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName SysRoleService
 * @Description
 * @Date 2022/3/5
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysRoleService {

    private final SysRoleDao sysRoleDao;

    public Page<SysRole> getRolePage(SysRole sysRole) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        Page<SysRole> page = SelectDomain.buildPage();
        String roleName = sysRole.getRoleName();
        if (StrUtil.isNotEmpty(roleName)) {
            wrapper.and(w -> w.like("role_name", roleName)
                    .or().like("role", roleName));
        }
        wrapper.orderByAsc("index_num");
        return sysRoleDao.selectPage(page, wrapper);
    }

    public List<SysRole> getRoleList() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("index_num");
        return sysRoleDao.selectList(wrapper);
    }

    public int addRole(SysRole sysRole) {
        return sysRoleDao.insert(sysRole);
    }

    public SysRole getRoleById(String roleId) {
        return sysRoleDao.selectById(roleId);
    }

    public int editRole(SysRole sysRole) {
        return sysRoleDao.updateById(sysRole);
    }

    public int delById(String id) {
        if ("1".equals(id)) {
            return -1;
        }
        return sysRoleDao.deleteById(id);
    }

    public int delByIds(List<String> ids) {
        if (ids.contains("1")) {return -1;}
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return sysRoleDao.delete(wrapper);
    }
}
