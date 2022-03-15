package com.archiveboot;

import cn.hutool.core.collection.CollUtil;
import com.archiveboot.dao.SysMenuDao;
import com.archiveboot.dao.SysRoleDao;
import com.archiveboot.entity.SysMenu;
import com.archiveboot.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName ApplicationTest
 * @Description TODO
 * @Date 2022/3/13
 */
@SpringBootTest
public class MyApplicationTest {

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Test
    void insertTest() {
        SysRole role = SysRole.builder()
                .role("manager")
                .roleName("管理员")
                .permissions(CollUtil.newHashSet("home", "table")).build();
        System.out.println(role);
        sysRoleDao.insert(role);
    }

    @Test
    void getTest() {
        List<SysRole> sysRoles = sysRoleDao.selectList(null);
        System.out.println(sysRoles);
    }
}
