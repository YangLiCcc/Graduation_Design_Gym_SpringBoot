package com.yang.springboot.service;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Instrument;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
public interface InstrumentService extends IService<Instrument> {

    Result getInstrumentPage(Integer currentPage, Integer pageSize, String name);

    Result getUnusedInstrument();

    Result insertInstrument(Instrument instrument);

    Result updateInstrument(Instrument instrument);

    Result deleteInstrumentById(Long id);

    Result deleteInstrumentBatchByIds(List<Long> ids);
}
