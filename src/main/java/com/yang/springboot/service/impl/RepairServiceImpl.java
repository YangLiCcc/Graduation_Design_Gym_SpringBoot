package com.yang.springboot.service.impl;

import com.yang.springboot.entity.Repair;
import com.yang.springboot.mapper.RepairDao;
import com.yang.springboot.service.RepairService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairDao, Repair> implements RepairService {

}
