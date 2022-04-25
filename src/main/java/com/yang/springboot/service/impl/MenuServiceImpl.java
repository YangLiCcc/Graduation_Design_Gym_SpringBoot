package com.yang.springboot.service.impl;

import com.yang.springboot.entity.Menu;
import com.yang.springboot.mapper.MenuDao;
import com.yang.springboot.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

}
