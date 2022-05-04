package com.arslinthboot;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.arslinthboot.config.redis.RedisTool;
import com.arslinthboot.dao.*;
import com.arslinthboot.entity.*;
import com.arslinthboot.service.SysUserService;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.arslinthboot.common.Constants.LOGIN_TOKEN_KEY;
import static com.arslinthboot.common.Constants.RESET_CODE;

/**
 * @author Arslinth
 * @ClassName ApplicationTest
 * @Description
 * @Date 2022/3/13
 */
@SpringBootTest
public class MyApplicationTest {

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysDictDao sysDictDao;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTool redisTool;

    @Autowired
    private SysJobDao sysJobDao;

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

    @Test
    void redisTest() {
        Set<String> strings = redisTool.scanKeys(LOGIN_TOKEN_KEY);
        System.out.println(strings);
    }

    @Test
    void booleanTest() {
        SysJob build = SysJob.builder()
                .jobName("测试").jobGroup("测试组").invokeTarget("2312312").status(false).build();
        sysJobDao.insert(build);
    }


    public static void main(String[] args) {
        // 生成 16 位随机 AES 密钥
//        String randomKey = AES.generateRandomKey();

    }
}
