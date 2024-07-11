package com.southwind.mapper;

import com.southwind.entity.Dormitory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface DormitoryMapper extends BaseMapper<Dormitory> {
    public void subAvailable(Integer id);
    public void addAvailable(Integer id);
    public Integer findAvailableDormitoryId();
    public Integer findAvailableDormitoryId2(Integer id);
}
