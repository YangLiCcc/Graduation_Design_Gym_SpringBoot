package com.yang.springboot.mapper;

import com.yang.springboot.entity.Role;
import com.yang.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.springboot.param.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    Role getRoleByUserId(Long userId);
}
