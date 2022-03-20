package com.arslinthboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.arslinthboot.entity.SysUser;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2021/7/25
 */
@Repository
public interface SysUserDao extends BaseMapper<SysUser> {

    @Update("UPDATE sys_user SET nick_name=#{nickName}, phone=#{phone},email=#{email},avatar=#{avatar},sex=#{sex},state = #{state} WHERE id = #{id}")
    int changeUserInfo(SysUser user);

    @Update("UPDATE sys_user SET state = #{state} WHERE id = #{id}")
    int setState(SysUser user);
}
