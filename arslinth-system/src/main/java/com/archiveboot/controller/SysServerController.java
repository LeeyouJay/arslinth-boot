package com.archiveboot.controller;

import com.archiveboot.common.ApiResponse;
import com.archiveboot.entity.SysServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.archiveboot.common.ResponseCode.SUCCESS;

/**
 * @author Lujb
 * @ClassName SysServerController
 * @Description TODO
 * @Date 2021/7/27
 */
@Slf4j
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysServerController {

    @GetMapping("/getServer")
    @PreAuthorize("hasAnyAuthority('sys_server')")
    public ApiResponse getInfo() throws Exception {
        SysServer server = new SysServer();
        server.copyTo();
        return ApiResponse.code(SUCCESS).data("server", server).message("读取成功");
    }
}
