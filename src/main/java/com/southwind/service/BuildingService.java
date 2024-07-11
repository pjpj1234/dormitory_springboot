package com.southwind.service;

import com.southwind.entity.Building;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.form.SearchForm;
import com.southwind.vo.PageVO;

import java.util.List;

public interface BuildingService extends IService<Building> {
    //这个是自定义的查询对应页列表的
    public PageVO list(Integer page,Integer size);
    public PageVO search(SearchForm searchForm);
    public Boolean deleteById(Integer id);
}
