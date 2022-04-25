package com.yang.springboot.controller;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.param.LockerSaveParam;
import com.yang.springboot.param.LockerUseParam;
import com.yang.springboot.service.LockerService;
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
@RequestMapping("/locker")
public class LockerController {

    private final LockerService lockerService;

    @Autowired
    public LockerController(LockerService lockerService) {
        this.lockerService = lockerService;
    }

    @GetMapping("/get/page")
    public Result getLockerPage(@RequestParam Integer currentPage,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String sex,
                                @RequestParam(defaultValue = "") String area) {
        return lockerService.getLockerPage(currentPage, pageSize, name, sex, area);
    }

    @PostMapping("/insert")
    public Result insertLocker(@RequestBody LockerSaveParam lockerSaveParam) {
        return lockerService.insertLocker(lockerSaveParam);
    }

    @PostMapping("/delete/{id}")
    public Result deleteLockerById(@PathVariable Long id) {
        return lockerService.deleteLockerById(id);
    }

    @PostMapping("/delete/batch/{ids}")
    public Result deleteLockerBatchByIds(@PathVariable List<Long> ids) {
        return lockerService.deleteLockerBatchByIds(ids);
    }

    @PostMapping("/update")
    public Result updateLocker(@RequestBody LockerSaveParam lockerSaveParam) {
        return lockerService.updateLocker(lockerSaveParam);
    }

    @PostMapping("/get/unused")
    public Result getUnusedLockerList() {
        return lockerService.getUnusedLockerList();
    }

    @PostMapping("/use")
    public Result useLockerByUserId(@RequestBody LockerUseParam lockerUseParam) {
        return lockerService.useLockerByUserId(lockerUseParam);
    }

}
