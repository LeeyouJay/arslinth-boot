package com.archiveboot.service;

import com.archiveboot.dao.SysRoleDao;
import com.archiveboot.entity.SysRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Arslinth
 * @ClassName SysRoleService
 * @Description TODO
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
