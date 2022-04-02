package com.arslinthboot.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.SysMenuDao;
import com.arslinthboot.entity.SysMenu;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Arslinth
 * @ClassName SysMenuService
 * @Description 裁断数据管理
 * @Date 2022/3/13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuService {

    private final SysMenuDao sysMenuDao;

    public List<SysMenu> getMenuList(Set<String> permissions, String menuName, boolean isRoutes) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq(isRoutes, "hidden", false)
                .ne(isRoutes, "menu_type", "F")
                .orderByAsc("index_num");
        if (StrUtil.isNotEmpty(menuName)) {
            wrapper.like("label", menuName);
        }
        if (CollUtil.isNotEmpty(permissions) && !permissions.contains("*")){
            wrapper.in("name",permissions);
        }

        List<SysMenu> list = sysMenuDao.selectList(wrapper);
        return list.stream().map(m -> {
            SysMenu.Meta meta = SysMenu.Meta.builder()
                    .title(m.getLabel())
                    .icon(m.getIcon())
                    .link(m.getLink())
                    .keepAlive(m.getKeepAlive()).build();
            m.setMeta(meta);
            return m;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public int addMenu(SysMenu menu) {
        if ("0".equals(menu.getParentId()) || "F".equals(menu.getMenuType())) {
            return sysMenuDao.insert(menu);
        }
        SysMenu parentMenu = sysMenuDao.selectById(menu.getParentId());
        if ("0".equals(parentMenu.getParentId())) {
            parentMenu.setComponent("Layout");
        } else {
            parentMenu.setComponent("ParentView");
        }
        parentMenu.setMenuType("M");
        sysMenuDao.updateById(parentMenu);
        return sysMenuDao.insert(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    public int editMenu(SysMenu menu) {
        if ("0".equals(menu.getParentId()) || "F".equals(menu.getMenuType())) {
            return sysMenuDao.updateById(menu);
        }
        SysMenu parentMenu = sysMenuDao.selectById(menu.getParentId());
        if ("0".equals(parentMenu.getParentId())) {
            parentMenu.setComponent("Layout");
        } else {
            parentMenu.setComponent("ParentView");
        }
        parentMenu.setMenuType("M");
        sysMenuDao.updateById(parentMenu);
        return sysMenuDao.updateById(menu);
    }

    public int delMenu(String id) {
        return sysMenuDao.deleteById(id);
    }

    public SysMenu getMenuById(String id) {
        return sysMenuDao.selectById(id);
    }

}
