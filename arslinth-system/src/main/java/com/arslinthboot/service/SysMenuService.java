package com.arslinthboot.service;

import com.arslinthboot.dao.SysMenuDao;
import com.arslinthboot.entity.SysMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName SysMenuService
 * @Description TODO
 * @Date 2022/3/13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuService {

    private final SysMenuDao sysMenuDao;

    public List<SysMenu> getMenuList() {
        return sysMenuDao.selectList(null);
    }

    public int addMenu(SysMenu menu) {
        return sysMenuDao.insert(menu);
    }
}
