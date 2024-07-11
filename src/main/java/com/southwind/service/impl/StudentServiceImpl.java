package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.southwind.entity.Dormitory;
import com.southwind.entity.DormitoryAdmin;
import com.southwind.entity.Student;
import com.southwind.entity.College;
import com.southwind.entity.Building;
import com.southwind.entity.*;
import com.southwind.form.RuleForm;
import com.southwind.form.SearchForm;
import com.southwind.form.StudentForm;
import com.southwind.mapper.DormitoryMapper;
import com.southwind.mapper.StudentMapper;
import com.southwind.mapper.CollegeMapper;
import com.southwind.mapper.BuildingMapper;
import com.southwind.mapper.*;
import com.southwind.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.util.CommonUtil;
import com.southwind.vo.PageVO;
import com.southwind.vo.ResultVO;
import com.southwind.vo.StudentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private DormitoryMapper dormitoryMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private DormitoryAdminMapper dormitoryAdminMapper;
    @Autowired
    private MoveoutMapper moveoutMapper;

    @Override
    public ResultVO login(RuleForm ruleForm) {
        //1、判断用户名是否存在
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", ruleForm.getUsername());
        Student student = this.studentMapper.selectOne(queryWrapper);
        ResultVO resultVO = new ResultVO();
        if(student == null){
            resultVO.setCode(-1);
        } else {
            //2、判断密码是否正确
            if(!student.getPassword().equals(ruleForm.getPassword())){
                resultVO.setCode(-2);
            } else {
                resultVO.setCode(0);
                resultVO.setData(student);
            }
        }
        return resultVO;
    }

    @Override
    public Boolean saveStudent(Student student) {
        //添加学生数据
        student.setCreateDate(CommonUtil.createDate());
//        int insert = this.studentMapper.insert(student);
//        if(insert != 1) return false;

        // 使用 selectList 方法获取所有匹配的建筑，
        String studentGender = student.getGender();
        List<Building> buildings = this.buildingMapper.selectList(new QueryWrapper<Building>()
                .eq("introduction", student.getIntroduction())
                .eq("gender", studentGender));
        // 遍历选择一个不是当前的宿舍
        Building selectedBuilding = null;
        for (Building building : buildings) {
            Integer curDormitoryId = this.dormitoryMapper.findAvailableDormitoryId2(building.getId());
            if (curDormitoryId != null) {
                selectedBuilding = building;
                break;
            }
        }
        if (selectedBuilding == null) {
            return false; // 没有找到合适的楼栋
        }
        Integer availableDormitoryId = this.dormitoryMapper.findAvailableDormitoryId2(selectedBuilding.getId());

        student.setDormitoryId(availableDormitoryId);
        int insert = this.studentMapper.insert(student);
        if(insert != 1) return false;

        //修改宿舍数据
        Dormitory dormitory = this.dormitoryMapper.selectById(availableDormitoryId);
        if (dormitory.getAvailable() == 0) {
            return false;
        }
        dormitory.setAvailable(dormitory.getAvailable() - 1);
        int update = this.dormitoryMapper.updateById(dormitory);
        if(update != 1) return false;
        return true;
    }

    @Override
    public PageVO list(Integer page, Integer size) {
        Page<Student> studentPage = new Page<>(page, size);
        Page<Student> resultPage = this.studentMapper.selectPage(studentPage, null);
        List<Student> studentList = resultPage.getRecords();
        //VO转换
        List<StudentVO> studentVOList = new ArrayList<>();
        for (Student student : studentList) {
            StudentVO studentVO = new StudentVO();
            BeanUtils.copyProperties(student, studentVO);
            Dormitory dormitory = this.dormitoryMapper.selectById(student.getDormitoryId());
            studentVO.setDormitoryName(dormitory.getName());
            College college = this.collegeMapper.selectById(student.getIntroduction());
            studentVO.setCollegeName(college.getName());
            Building building = this.buildingMapper.selectById(dormitory.getBuildingId());
            studentVO.setBuildingName(building.getName());

            studentVOList.add(studentVO);
        }
        PageVO pageVO = new PageVO();
        pageVO.setData(studentVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public PageVO search(SearchForm searchForm) {
        Page<Student> studentPage = new Page<>(searchForm.getPage(), searchForm.getSize());
        Page<Student> resultPage = null;
        if(searchForm.getValue().equals("")){
            resultPage = this.studentMapper.selectPage(studentPage, null);
        } else {
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            if(searchForm.getKey().equals("collegeName")){
                QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
                collegeQueryWrapper.like("name", searchForm.getValue());
                List<College> collegeList = this.collegeMapper.selectList(collegeQueryWrapper);
                List<Integer> idList = new ArrayList<>();
                for (College college : collegeList) {
                    idList.add(college.getId());
                }
                if (idList.isEmpty()) {
                    queryWrapper.eq("introduction", -1);
                } else {
                    queryWrapper.in("introduction", idList);
                }
            }
            else if(searchForm.getKey().equals("buildingName")){
                QueryWrapper<Building> buildingQueryWrapper = new QueryWrapper<>();
                buildingQueryWrapper.like("name", searchForm.getValue());
                List<Building> buildingList = this.buildingMapper.selectList(buildingQueryWrapper);

                List<Dormitory> dormitoryList = new ArrayList<>();
                for (Building building : buildingList) {
                    QueryWrapper<Dormitory> dormitoryQueryWrapper = new QueryWrapper<>();
                    dormitoryQueryWrapper.eq("building_id", building.getId());
                    List<Dormitory> buildingDormitories = this.dormitoryMapper.selectList(dormitoryQueryWrapper);
                    dormitoryList.addAll(buildingDormitories);
                }

                List<Integer> idList = new ArrayList<>();
                for (Dormitory dormitory :  dormitoryList) {
                    idList.add(dormitory.getId());
                }
                if (idList.isEmpty()) {
                    queryWrapper.eq("dormitory_id", -1);
                } else {
                    queryWrapper.in("dormitory_id", idList);
                }
            }
            else{
                queryWrapper.like(searchForm.getKey(), searchForm.getValue());
            }
            resultPage = this.studentMapper.selectPage(studentPage, queryWrapper);
        }
        List<Student> studentList = resultPage.getRecords();
        //VO转换
        List<StudentVO> studentVOList = new ArrayList<>();
        for (Student student : studentList) {
            StudentVO studentVO = new StudentVO();
            BeanUtils.copyProperties(student, studentVO);
            Dormitory dormitory = this.dormitoryMapper.selectById(student.getDormitoryId());
            studentVO.setDormitoryName(dormitory.getName());
            College college = this.collegeMapper.selectById(student.getIntroduction());
            studentVO.setCollegeName(college.getName());
            Building building = this.buildingMapper.selectById(dormitory.getBuildingId());
            studentVO.setBuildingName(building.getName());

            studentVOList.add(studentVO);
        }
        PageVO pageVO = new PageVO();
        pageVO.setData(studentVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public Boolean update(StudentForm studentForm) {
        //更新学生信息
        Student student = new Student();
        BeanUtils.copyProperties(studentForm, student);
        int update = this.studentMapper.updateById(student);
        if(update != 1) return false;
        //更新宿舍数据
        if(!studentForm.getDormitoryId().equals(studentForm.getOldDormitoryId())){
            //old+1，new-1
            try {
                this.dormitoryMapper.addAvailable(studentForm.getOldDormitoryId());
                this.dormitoryMapper.subAvailable(studentForm.getDormitoryId());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean deleteById(Integer id) {
        //修改宿舍数据
        Student student = this.studentMapper.selectById(id);
        try {
            Dormitory dormitory = this.dormitoryMapper.selectById(student.getDormitoryId());
            if(dormitory.getType() > dormitory.getAvailable()){
                this.dormitoryMapper.addAvailable(student.getDormitoryId());
            }
        } catch (Exception e) {
            return false;
        }
        //删除学生数据
        int Delete = this.moveoutMapper.delete(new QueryWrapper<Moveout>().eq("student_id", id));
        int delete = this.studentMapper.deleteById(id);
        if(delete != 1 && Delete != 1) return false;
        return true;
    }
}
