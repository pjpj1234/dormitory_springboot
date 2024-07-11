package com.southwind.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.Dormitory;
import com.southwind.entity.Student;
import com.southwind.entity.*;
import com.southwind.form.RuleForm;
import com.southwind.form.SearchForm;
import com.southwind.form.StudentForm;
import com.southwind.service.*;
import com.southwind.service.StudentService;
import com.southwind.util.ResultVOUtil;
import com.southwind.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DormitoryService dormitoryService;

    @GetMapping("/login")
    public ResultVO login(RuleForm ruleForm){
        ResultVO resultVO = this.studentService.login(ruleForm);
        return resultVO;
    }

    @PostMapping("/save")
    public ResultVO save(@RequestBody Student student){
        Boolean saveStudent = this.studentService.saveStudent(student);
        if(!saveStudent) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        return ResultVOUtil.success(this.studentService.list(page, size));
    }

    @GetMapping("/search")
    public ResultVO search(SearchForm searchForm){
        return ResultVOUtil.success(this.studentService.search(searchForm));
    }

    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id){
        Student student = this.studentService.getById(id);
        StudentForm studentForm = new StudentForm();
        Dormitory dormitory = this.dormitoryService.getById(student.getDormitoryId());
        studentForm.setBuildingId(dormitory.getBuildingId());
        BeanUtils.copyProperties(student, studentForm);
        studentForm.setOldDormitoryId(student.getDormitoryId());
        return ResultVOUtil.success(studentForm);
    }

    @GetMapping("/findBuildingByIntroduction/{id}/{gender}")
    public ResultVO findBuildingByIntroduction(@PathVariable("id") Integer id,@PathVariable("gender") String gender){
        QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("introduction", id);
        queryWrapper.eq("gender", gender);
        List<Building> buildingList = this.buildingService.list(queryWrapper);
        return ResultVOUtil.success(buildingList);
    }

    @GetMapping("/findDormitoryByBuildingId/{id}")
    public ResultVO findDormitoryByBuildingId(@PathVariable("id") Integer id){
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("building_id", id);
        queryWrapper.gt("available", 0);
        List<Dormitory> dormitoryList = this.dormitoryService.list(queryWrapper);
        return ResultVOUtil.success(dormitoryList);
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody StudentForm studentForm){
        Boolean update = this.studentService.update(studentForm);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultVO deleteById(@PathVariable("id") Integer id){
        Boolean delete = this.studentService.deleteById(id);
        if(!delete) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }
}

