package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.southwind.entity.Building;
import com.southwind.entity.College;
import com.southwind.entity.Student;
import com.southwind.form.SearchForm;
import com.southwind.mapper.BuildingMapper;
import com.southwind.mapper.CollegeMapper;
import com.southwind.mapper.StudentMapper;
import com.southwind.service.BuildingService;
import com.southwind.service.CollegeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.vo.CollegeVO;
import com.southwind.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;
}
