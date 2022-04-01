package com.arslinthboot.controller;

import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @author Lujb
 * @ClassName SysServerController
 * @Description 系统信息
 * @Date 2021/7/27
 */
@Slf4j
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysServerController {

    /**
     * 获取系统信息
     */
    @GetMapping("/getServer")
    @PreAuthorize("hasAnyAuthority('sys_server')")
    public ApiResponse getInfo() throws Exception {
        SysServer server = new SysServer();
        server.copyTo();
        return ApiResponse.code(SUCCESS).data("server", server).message("读取成功");
    }
}
