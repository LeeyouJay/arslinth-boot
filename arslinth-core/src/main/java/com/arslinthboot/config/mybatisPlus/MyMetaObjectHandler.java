package com.arslinthboot.config.mybatisPlus;

import com.arslinthboot.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Arslinth
 * @ClassName MyMetaObjectHandler
 * @Description 自动加入更新时间和创建时间
 * @Date 2021/7/25
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        if (metaObject.hasGetter("createBy")) {
            this.setFieldValByName("createBy", SecurityUtils.getUserId(), metaObject);
        }
        if (metaObject.hasGetter("createTime")) {
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateBy")) {
            this.setFieldValByName("updateBy", SecurityUtils.getUserId(), metaObject);
        }
        if (metaObject.hasGetter("updateTime")) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

}
