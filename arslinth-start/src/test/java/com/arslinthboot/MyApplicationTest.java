package com.arslinthboot;

import cn.hutool.core.collection.CollUtil;
import com.arslinthboot.dao.SysDictDao;
import com.arslinthboot.dao.SysMenuDao;
import com.arslinthboot.dao.SysRoleDao;
import com.arslinthboot.dao.SysUserDao;
import com.arslinthboot.entity.SysDict;
import com.arslinthboot.entity.SysMenu;
import com.arslinthboot.entity.SysRole;
import com.arslinthboot.entity.SysUser;
import com.arslinthboot.service.SysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.arslinthboot.common.Constants.RESET_CODE;

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

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysDictDao sysDictDao;

    @Autowired
    private SysUserService sysUserService;

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
        Page<SysDict> sysDictPage = sysDictDao.selectPage(new Page<>(), null);
        System.out.println(sysDictPage.getRecords().size());
    }
    @Test
    void sysUserHandle(){
        SysUser sysUser = SysUser.builder().roleIds(CollUtil.newHashSet("1502756885088612354"))
                .username("username")
                .phone("18677952830")
                .nickName("Arslinth")
                .permissions(CollUtil.newHashSet("Home", "UserCenter", "SysMenu", "SysUser", "SysRole", "SysDict", "Loginlog", "Operlog"))
                .build();

        sysUserDao.insert(sysUser);
    }

    @Test
    void restPasswordTest() {
        SysUser sysUser = sysUserDao.selectById("1506522728280870914");
        sysUser.setPassword(new BCryptPasswordEncoder().encode(RESET_CODE));
        sysUserDao.updateById(sysUser);
    }

    @Test
    void selectTest() {
        Page<SysUser> userPage = sysUserService.getUserPage(new SysUser());
        System.out.println(userPage.getRecords());

    }

    public static void main(String[] args) {

        ArrayList<String> oldIds = CollUtil.newArrayList("A", "B", "C", "A", "C", "A", "D", "E", "F", "D");

        ArrayList<String> ids = CollUtil.newArrayList( "E", "F", "D", "Y", "Z");
        ids.removeAll(oldIds);
        System.out.println(ids);
//        Map<String, String> map = oldIds.stream().collect(Collectors.toMap(Function.identity(), Function.identity(), (v1, v2) -> v2));
//
//        List<String> collect = ids.stream().filter(f -> !map.containsKey(f)).collect(Collectors.toList());
//        System.out.println(collect);
    }
}
