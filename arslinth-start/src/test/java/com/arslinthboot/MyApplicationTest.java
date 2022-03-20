package com.arslinthboot;

import com.arslinthboot.dao.SysMenuDao;
import com.arslinthboot.dao.SysRoleDao;
import com.arslinthboot.entity.SysMenu;
import com.arslinthboot.entity.SysRole;
import org.jasypt.encryption.StringEncryptor;
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

    @Autowired
    private StringEncryptor encryptor;


    @Test
    void insertTest() {
        SysMenu menu = SysMenu.builder()
                .path("system")
                .component("Layout")
                .menuType("M")
                .name("System")
                .meta(SysMenu.Meta.builder().title("系统设置").icon("vue-dsn-icon-biaoge").build())
                .indexNum(0).build();
        sysMenuDao.insert(menu);
    }

    @Test
    void updateTest() {
        SysMenu sysMenu = sysMenuDao.selectById("1505459289445818371");
        sysMenu.getMeta().setKeepAlive(true);
        sysMenuDao.updateById(sysMenu);
    }

    @Test
    void getTest() {
        List<SysRole> sysRoles = sysRoleDao.selectList(null);
        System.out.println(sysRoles);
    }

    @Test
    void encryptionTest() {
        String name = encryptor.encrypt("arslinth-boot");
        String password = encryptor.encrypt("spring-arslinth");
        String redisPassword = encryptor.encrypt("Arslinth");
        System.out.println(name);
        System.out.println(password);
        System.out.println(redisPassword);
        System.out.println(encryptor.encrypt("root"));

    }
}
