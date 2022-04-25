package com.yang.springboot.mapper;

import com.yang.springboot.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-16
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {

    List<String> selectPermissionsByUserRoleId(Integer roleId);
}
