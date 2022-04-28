package com.arslinthboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysUploadFile;
import com.arslinthboot.service.SysUploadFileService;
import com.arslinthboot.utils.HttpServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @author Arslinth
 * @ClassName SysUploadFileController
 * @Description
 * @Date 2022/4/29
 */
@RestController
@RequestMapping("/sysFile")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUploadFileController {

    private final SysUploadFileService sysUploadFileService;


    /**
     * 获取文件真实名称
     */
    @GetMapping("/getFileNameById/{fileId}")
    public ApiResponse getFileNameById(@PathVariable String fileId) {
        SysUploadFile file = sysUploadFileService.getFileById(fileId);
        if (file == null) {
            return ApiResponse.code(FAIL).message("文件不存在！");
        }
        return ApiResponse.code(SUCCESS).data("filename", file.getFileName());
    }

    /**
     * 统一文件下载
     **/
    @GetMapping("/downloadFile/{fileId}")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileId) {
        SysUploadFile sysFile = sysUploadFileService.getFileById(fileId);
        if (sysFile == null) {
            HttpServletUtil.print(response, JSON.toJSONString(ApiResponse.code(FAIL).message("找不到文件！")));
            return;
        }
        File file = new File(sysFile.getFilePath());
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;
        try {
            String fileName = URLEncoder.encode(sysFile.getFileName(), StandardCharsets.UTF_8);
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            bis = new BufferedInputStream(inputStream);
            int i = bis.read(buff);
            os = response.getOutputStream();
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
            HttpServletUtil.print(response, JSON.toJSONString(ApiResponse.code(FAIL).message("文件读取错误！")));
        } finally {
            IoUtil.close(bis);
        }

    }
}
