package com.yang.springboot.mapper;

import com.yang.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Mapper
public interface RoleDao extends BaseMapper<Role> {

}
