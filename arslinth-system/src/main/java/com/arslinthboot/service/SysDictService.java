package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.SysDictDao;
import com.arslinthboot.entity.SysDict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName SysDictService
 * @Description TODO
 * @Date 2022/3/28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysDictService {

    private final SysDictDao sysDictDao;

    public Page<SysDict> getTypePage(SysDict sysDict) {
        Page<SysDict> page = new Page<>(sysDict.getPageIndex(), sysDict.getPageSize());
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_Id", "0");
        if (StrUtil.isNotEmpty(sysDict.getDictName()) || StrUtil.isNotEmpty(sysDict.getDictValue())) {
            wrapper.and(w -> w.like("dict_name", sysDict.getDictName())
                    .or().like("dict_value", sysDict.getDictValue()));
        }
        return sysDictDao.selectPage(page, wrapper);
    }


    public Page<SysDict> getValuePage(SysDict sysDict) {
        Page<SysDict> page = new Page<>(sysDict.getPageIndex(), sysDict.getPageSize());
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.ne("parent_Id", "0");
        if (StrUtil.isNotEmpty(sysDict.getParentId()) || StrUtil.isNotEmpty(sysDict.getDictValue())) {
            wrapper.eq("parent_Id", sysDict.getParentId())
                    .and(w -> w.like("dict_value", sysDict.getDictValue()));
        }
        return sysDictDao.selectPage(page, wrapper);
    }

    public List<SysDict> getValueList(){
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.ne("parent_Id", "0");
        return sysDictDao.selectList(wrapper);
    }


    public void addDict(SysDict sysDict) {
        sysDictDao.insert(sysDict);
    }

    public int editDict(SysDict sysDict) {
        return sysDictDao.updateById(sysDict);
    }

    public int delById(String id) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<SysDict> dicts = sysDictDao.selectList(wrapper);
        if (dicts.size() > 0) {
            return -1;
        }
        return sysDictDao.deleteById(id);
    }


}