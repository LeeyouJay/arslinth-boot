package com.arslinthboot.service;

import com.arslinthboot.dao.SysRoleDao;
import com.arslinthboot.entity.SysRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public SysRole getRoleById(String roleId) {
        return sysRoleDao.selectById(roleId);
    }
}
