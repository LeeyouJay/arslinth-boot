package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.arslinthboot.dao.SysLoginDao;
import com.arslinthboot.entity.SysLogin;
import com.arslinthboot.entity.VO.QueryBody;
import com.arslinthboot.utils.IpInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @className: SysLoginService
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2022/1/5
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLoginService {

    private final SysLoginDao sysLoginDao;

    public Page<SysLogin> getLoginPageList(QueryBody query) {
        Page<SysLogin> page = new Page<>(query.getPageIndex(), query.getPageSize());
        QueryWrapper<SysLogin> wrapper = new QueryWrapper<>();
        String keyWords = query.getSearchName();
        String state = query.getState();
        if (StrUtil.isNotEmpty(state)) {
            wrapper.eq("state", state);
        }

        if (StrUtil.isNotEmpty(keyWords)) {
            wrapper.and(w -> w.like("username", keyWords)
                    .or().like("ip_addr", keyWords));
        }
        wrapper.orderByDesc("create_time");
        return sysLoginDao.selectPage(page, wrapper);
    }

    public void saveLogin(HttpServletRequest request, SysLogin sysLogin) {

        try {
            // 获取ip地址
            String ipAddr = IpInfoUtil.getIpAddr(request);
            // 获取ip来源
            String ipSource = IpInfoUtil.getipSource(ipAddr);
            //获取浏览器信息
            String browser = IpInfoUtil.getBrowser(request);
            // 获取系统名称
            String sysName = IpInfoUtil.getSystemName(request);

            sysLogin.setBrowser(browser);
            sysLogin.setIpAddr(ipAddr);
            sysLogin.setIpSource(ipSource);
            sysLogin.setSysName(sysName);
            sysLoginDao.insert(sysLogin);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取ip来源失败！");
        }
    }

    public int delLoginByIds(List<String> ids) {
        return sysLoginDao.deleteBatchIds(ids);
    }

    public int delAll() {
        return sysLoginDao.delete(null);
    }

//    public SysLogin getLastLogin(){
//        ArchivesUser empUser = LoginUserUtil.getEmpUser();
//        String empId = Optional.ofNullable(empUser.getEmpId()).orElse("66482218");
//        QueryWrapper<SysLogin> wrapper = new QueryWrapper<>();
//        wrapper.le("create_time",new Date()).eq("emp_id",empId)
//                .orderByDesc("create_time").last("LIMIT 1,1");
//        List<SysLogin> sysLogins = sysLoginDao.selectList(wrapper);
//        if (sysLogins.size() == 0) {
//            return new SysLogin();
//        }
//        return sysLogins.get(0);
//    }

}
