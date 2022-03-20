package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.arslinthboot.dao.SysLogDao;
import com.arslinthboot.entity.SysLog;
import com.arslinthboot.entity.VO.QueryBody;
import com.arslinthboot.utils.IpInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @className: SysLogService
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogService {

    private final SysLogDao sysLogDao;

    public void saveLog(HttpServletRequest request, SysLog sysLog) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userStr = authentication.getCredentials().toString();
        String username = authentication.getPrincipal().toString();
        do {
            if (StrUtil.isEmpty(userStr)) {
                break;
            }
            JSONObject jsonObject = JSON.parseObject(userStr);
            username = Optional.ofNullable(jsonObject.get("empName"))
                    .map(Object::toString).orElse("");
            if (StrUtil.isEmpty(username)) {
                username = jsonObject.get("nickName").toString();
            }
        } while (false);
        String methodName = sysLog.getMethod();
        String[] result = StrUtil.subBetweenAll(methodName, "controller.", "(");
        sysLog.setMethod(result[0]);
        // 获取ip地址
        String ipAddr = IpInfoUtil.getIpAddr(request);
        // 获取ip来源
        String ipSource = IpInfoUtil.getipSource(ipAddr);
        //获取浏览器信息
        String browser = IpInfoUtil.getBrowser(request);
        // 获取系统名称
        String sysName = IpInfoUtil.getSystemName(request);

        sysLog.setUsername(username);
        sysLog.setBrowser(browser);
        sysLog.setIpAddr(ipAddr);
        sysLog.setIpSource(ipSource);
        sysLog.setSysName(sysName);

        sysLogDao.insert(sysLog);
    }

    public Page<SysLog> getLogPageList(QueryBody query) {
        Page<SysLog> page = new Page<>(query.getPageIndex(), query.getPageSize());
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();
        String keyWords = query.getSearchName();
        if (StrUtil.isNotEmpty(keyWords)) {
            wrapper.like("username", keyWords)
                    .or().like("ip_addr", keyWords)
                    .or().like("method", keyWords);
        }
        wrapper.orderByDesc("create_time");
        return sysLogDao.selectPage(page, wrapper);
    }

    public int delLogByIds(List<String> ids) {
        return sysLogDao.deleteBatchIds(ids);
    }

    public int delAll() {
        return sysLogDao.delete(null);
    }
}
