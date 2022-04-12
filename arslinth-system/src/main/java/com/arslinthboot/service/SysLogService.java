package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.LoginLogDao;
import com.arslinthboot.dao.OperLogDao;
import com.arslinthboot.entity.LoginLog;
import com.arslinthboot.utils.SelectDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.arslinthboot.entity.OperLog;
import com.arslinthboot.utils.IpInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @className: SysLogService
 * @description:
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogService {

    private final OperLogDao operLogDao;

    private final LoginLogDao loginLogDao;

    /**
     * 保存操作日志
     **/
    @Async
    public void saveOperLog(OperLog operLog) {
        String methodName = operLog.getMethod();
        String[] result = StrUtil.subBetweenAll(methodName, "controller.", "(");
        operLog.setMethod(result[0]);
        // 获取ip地址
        String ipAddr = operLog.getIpAddr();
        // 获取ip来源
        String ipSource = null;
        try {
            ipSource = IpInfoUtil.getipSource(ipAddr);
        } catch (Exception e) {
            ipSource = "未知IP";
        }
        operLog.setIpSource(ipSource);
        operLogDao.insert(operLog);
    }
    /**
     * 保存登入日志
     *
     **/
    public void saveLoginLog(HttpServletRequest request, LoginLog loginLog) {
        try {
            // 获取ip地址
            String ipAddr = IpInfoUtil.getIpAddr(request);
            // 获取ip来源
            String ipSource = IpInfoUtil.getipSource(ipAddr);
            //获取浏览器信息
            String browser = IpInfoUtil.getBrowser(request);
            // 获取系统名称
            String sysName = IpInfoUtil.getSystemName(request);

            loginLog.setBrowser(browser);
            loginLog.setIpAddr(ipAddr);
            loginLog.setIpSource(ipSource);
            loginLog.setSysName(sysName);
            loginLogDao.insert(loginLog);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取ip来源失败！");
        }
    }

    public Page<OperLog> operLogPage(OperLog operLog) {
        QueryWrapper<OperLog> wrapper = new QueryWrapper<>();
        Page<OperLog> page = SelectDomain.buildPage();
        String username = operLog.getUsername();
        if (StrUtil.isNotEmpty(username)) {
            wrapper.like("username", username)
                    .or().like("ip_addr", username)
                    .or().like("method", username);
        }
        if (operLog.getBeginTime() != null && operLog.getEndTime() != null) {
            wrapper.between("create_time", operLog.getBeginTime(), operLog.getEndTime());
        }
        wrapper.orderByDesc("create_time");
        return operLogDao.selectPage(page, wrapper);
    }

    public Page<LoginLog> loginLogPage(LoginLog loginLog) {
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>();
        Page<LoginLog> page = SelectDomain.buildPage();
        Boolean state = loginLog.getState();
        String username = loginLog.getUsername();
        if (state != null) {
            wrapper.eq("state", state);
        }
        if (loginLog.getBeginTime() != null && loginLog.getEndTime() != null) {
            wrapper.between("create_time", loginLog.getBeginTime(), loginLog.getEndTime());
        }
        if (StrUtil.isNotEmpty(username)) {
            wrapper.and(w -> w.like("username", username)
                    .or().like("ip_addr", username));
        }
        wrapper.orderByDesc("create_time");
        return loginLogDao.selectPage(page, wrapper);
    }


    public int delOperLogByIds(List<String> ids) {
        return operLogDao.deleteBatchIds(ids);
    }


    public int delAllOperLog() {
        return operLogDao.delete(null);
    }

    public int delLoginLogByIds(List<String> ids) {
        return loginLogDao.deleteBatchIds(ids);
    }


    public int delAllLoginLog() {
        return loginLogDao.delete(null);
    }

}
