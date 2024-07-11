package com.southwind.mapper;

import com.southwind.entity.College;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CollegeMapper extends BaseMapper<College> {
    public Integer findAvailableCollegeId();
}
