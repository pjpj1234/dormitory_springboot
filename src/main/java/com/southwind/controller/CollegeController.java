package com.southwind.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.College;
import com.southwind.form.SearchForm;
import com.southwind.service.CollegeService;
import com.southwind.util.ResultVOUtil;
import com.southwind.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @GetMapping("/availableList")
    public ResultVO availableList(){
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id", 0);
        List<College> collegeList = this.collegeService.list(queryWrapper);
        return ResultVOUtil.success(collegeList);
    }
}
