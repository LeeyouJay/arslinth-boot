package com.arslinthboot;

import cn.hutool.core.collection.CollUtil;
import com.arslinthboot.dao.SysMenuDao;
import com.arslinthboot.dao.SysRoleDao;
import com.arslinthboot.dao.SysUserDao;
import com.arslinthboot.entity.SysMenu;
import com.arslinthboot.entity.SysRole;
import com.arslinthboot.entity.SysUser;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

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
    @Test
    void sysUserHandle(){
        SysUser sysUser = SysUser.builder().roleIds(CollUtil.newHashSet("1502756885088612354"))
                .username("username")
                .phone("18677952830")
                .nickName("Arslinth")
                .permissions(CollUtil.newHashSet("Home", "UserCenter","SysMenu","SysUser","SysRole","SysDict","Loginlog","Operlog"))
                .build();

        sysUserDao.insert(sysUser);
    }

     void  sortTest(){


     }

    public static void main(String[] args) {

        ArrayList<String> intiList = CollUtil.newArrayList("A", "B", "C", "A", "C", "A", "D", "E", "F", "D");

        Map<String, Long> map = intiList.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>();
        list.addAll(map.entrySet());

        List<String> collect = list.stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(m -> m.getKey()).collect(Collectors.toList());

//        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        System.out.println(collect);
    }
}
