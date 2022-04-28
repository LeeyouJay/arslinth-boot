package com.arslinthboot.service;

import com.arslinthboot.dao.SysUploadFileDao;
import com.arslinthboot.entity.SysUploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: SysUploadFileService
 * @description:
 * @author: Arslinth
 * @date: 2022/4/28
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUploadFileService {

    private final SysUploadFileDao sysUploadFileDao;

    public SysUploadFile getFileById(String id) {
        return sysUploadFileDao.selectById(id);
    }
}
