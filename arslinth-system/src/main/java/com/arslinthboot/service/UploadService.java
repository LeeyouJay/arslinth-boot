package com.arslinthboot.service;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.SysUploadFileDao;
import com.arslinthboot.entity.SysUploadFile;
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

    private final SysUploadFileDao sysUploadFileDao;

    /**
     * 上传图片
     * return 返回略缩图访问地址
     */
    public String uploadImg(MultipartFile file, String name) throws IOException {

        InputStream inputStream = file.getInputStream();

        String fileName = name + ".jpg";

        String saveToPath = fileProperty.getImgStaticPath();
        // 真实路径，实际储存的路径
        String realPath = fileProperty.getImgFolder();
        // 储存文件的物理路径，使用本地路径储存
        String filepath = realPath + fileName;

        System.out.println("上传图片名为：" + fileName + "--虚拟文件路径为：" + saveToPath + "--物理文件路径为：" + realPath);

        //保存文件
        saveFile(inputStream, filepath);

        //复制保存并缩小原图图片
        ThumbnailsUtil.generateThumbnail2Directory(fileProperty.getImgFolder() + fileName);
        //追加后缀
        String miniName = ThumbnailsUtil.appendSuffix(fileName, MINI_IMG_SUFFIX);
        // 返回虚拟路径，给链接访问
        return saveToPath + miniName;
    }

    /**
     * 上传文件
     * return 储存对象信息
     */
    public SysUploadFile uploadFile(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        //文件原名
        String originalFilename = multipartFile.getOriginalFilename();
        //文件扩展名
        String extraName = StrUtil.subAfter(originalFilename, ".", true);
        //文件保存路径
        String realPath = fileProperty.getUploadFolder();
        String filepath = realPath + RandomUtil.randomString(19) + "." + extraName;
        //保存文件
        File file = saveFile(inputStream, filepath);
        //文件类型
        String type = FileTypeUtil.getType(file);
        SysUploadFile sysUploadFile = SysUploadFile.builder()
                .fileName(originalFilename)
                .fileType(type)
                .filePath(filepath)
                .fileExtra(extraName).build();
        sysUploadFileDao.insert(sysUploadFile);
        return sysUploadFile;
    }

    private File saveFile(InputStream inputStream, String filepath) throws IOException {
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
        return new File(filepath);
    }

}
