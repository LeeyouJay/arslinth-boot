package com.arslinthboot.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.arslinthboot.common.ApiResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.arslinthboot.common.ResponseCode.FAIL;

/**
 * @className: ResponseUtil
 * @description: 客户端工具
 * @author: Arslinth
 * @date: 2022/2/11
 **/
public class HttpServletUtil {

    public static void print(HttpServletResponse response, Object... object) {
        PrintWriter writer = null;
        try {
            writer = utf8AndJson(response).getWriter();
            for (Object o : object) {
                writer.print(o);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 文件推流下载
     */
    public static void pushStream(HttpServletResponse response, File file, String fileName) {
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            BufferedInputStream inputStream = FileUtil.getInputStream(file);
            os = response.getOutputStream();
            fileName = URLEncoder.encode(fileName, CharsetUtil.UTF_8);
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            bis = new BufferedInputStream(inputStream);
            byte[] buff = new byte[1024];
            int i;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            print(response, JSON.toJSONString(ApiResponse.code(FAIL).message("文件读取错误！")));
        } catch (IORuntimeException ioe) {
            print(response, JSON.toJSONString(ApiResponse.code(FAIL).message("系统找不到指定的文件！")));
        } finally {
            IoUtil.close(bis);
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    // 静默关闭
                }
            }
        }
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取Request请求body内容
     *
     * @Param request
     **/
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try (InputStream inputStream = request.getInputStream()) {
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return sb.toString();
    }


    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }


    private static HttpServletResponse utf8AndJson(HttpServletResponse response) {
        response.setContentType("text/json;charset=utf-8");
        return response;
    }


}
