package com.yang.springboot.controller;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Repair;
import com.yang.springboot.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/repair")
public class RepairController {

    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/get/page")
    public Result getRepairPage(@RequestParam Integer currentPage,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String type,
                                @RequestParam(defaultValue = "") String name) {
        return repairService.getRepairPage(currentPage, pageSize, type, name);
    }

    @PostMapping("/insert")
    public Result insertRepairRecord(@RequestBody Repair repair) {
        return repairService.insertRecord(repair);
    }

    @PostMapping("/update")
    public Result updateRepairRecordById(@RequestBody Repair repair) {
        return repairService.updateRecordById(repair);
    }

}
