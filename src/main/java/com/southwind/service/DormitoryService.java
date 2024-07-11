package com.southwind.service;

import com.southwind.entity.Dormitory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.form.SearchForm;
import com.southwind.vo.PageVO;

public interface DormitoryService extends IService<Dormitory> {
    public PageVO list(Integer page, Integer size);
    public PageVO search(SearchForm searchForm);
    public Boolean deleteById(Integer id);
}
