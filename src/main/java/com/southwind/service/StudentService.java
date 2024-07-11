package com.southwind.service;

import com.southwind.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.form.RuleForm;
import com.southwind.form.SearchForm;
import com.southwind.form.StudentForm;
import com.southwind.vo.PageVO;
import com.southwind.vo.ResultVO;
import com.southwind.vo.StudentVO;

import java.util.List;

public interface StudentService extends IService<Student> {
    public ResultVO login(RuleForm ruleForm);
    public Boolean saveStudent(Student student);
    public PageVO list(Integer page, Integer size);
    public PageVO search(SearchForm searchForm);
    public Boolean update(StudentForm studentForm);
    public Boolean deleteById(Integer id);
}
