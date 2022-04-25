package com.yang.springboot.controller;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Instrument;
import com.yang.springboot.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/instrument")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/get/page")
    public Result getInstrumentPage(@RequestParam Integer currentPage,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String name) {

        return instrumentService.getInstrumentPage(currentPage, pageSize, name);
    }

    @GetMapping("/get/unused")
    public Result getUnusedInstrument() {
        return instrumentService.getUnusedInstrument();
    }

    @PostMapping("/insert")
    public Result insertInstrument(@RequestBody Instrument instrument) {
        return instrumentService.insertInstrument(instrument);
    }

    @PostMapping("/update")
    public Result updateInstrument(@RequestBody Instrument instrument) {
        return instrumentService.updateInstrument(instrument);
    }

    @PostMapping("/delete/{id}")
    public Result deleteInstrumentById(@PathVariable Long id) {
        return instrumentService.deleteInstrumentById(id);
    }

    @PostMapping("/delete/batch/{ids}")
    public Result deleteInstrumentBatchByIds(@PathVariable List<Long> ids) {
        return instrumentService.deleteInstrumentBatchByIds(ids);
    }

}
