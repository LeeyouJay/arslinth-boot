package com.arslinthboot.service;

import cn.hutool.core.io.FileUtil;
import com.arslinthboot.properties.FileProperty;
import com.arslinthboot.utils.ThumbnailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.arslinthboot.common.Constants.MINI_IMG_SUFFIX;

/**
 * @author Arslinth
 * @ClassName UploadService
 * @Description
 * @Date 2021/3/2
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(FileProperty.class)
public class UploadService {

    private final FileProperty fileProperty;


    public String uploadImg(MultipartFile file, String name) throws IOException {

        InputStream inputStream = file.getInputStream();

        String fileName = name + ".jpg";

        String saveToPath = fileProperty.getImgStaticPath();
        // 真实路径，实际储存的路径
        String realPath = fileProperty.getImgFolder();
        // 储存文件的物理路径，使用本地路径储存
        String filepath = realPath + fileName;

        System.out.println("上传图片名为：" + fileName + "--虚拟文件路径为：" + saveToPath + "--物理文件路径为：" + realPath);

        // 判断有没有对应的文件夹
        File destFile = new File(filepath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        //输出流 输出到文件
        OutputStream outputStream = new FileOutputStream(destFile);
        // 缓冲区
        byte[] bs = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(bs)) != -1) {
            outputStream.write(bs, 0, len);
        }
        inputStream.close();
        outputStream.close();

        //复制保存并缩小原图图片
        ThumbnailsUtil.generateThumbnail2Directory(fileProperty.getImgFolder() + fileName);
        //追加后缀
        String miniName = ThumbnailsUtil.appendSuffix(fileName, MINI_IMG_SUFFIX);
        // 返回虚拟路径，给链接访问
        return saveToPath + miniName;
    }


    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        String typename = filename.substring(filename.lastIndexOf("."));
        fileName = fileName + typename;

        String saveToPath = fileProperty.getUploadStaticPath();

        String realPath = fileProperty.getUploadStaticPath();

        String filepath = realPath + fileName;

        FileUtil.writeFromStream(inputStream, filepath);

        return saveToPath;
    }


}
