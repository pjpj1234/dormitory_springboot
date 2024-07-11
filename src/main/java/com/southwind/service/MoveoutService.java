package com.southwind.service;

import com.southwind.entity.Moveout;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.form.SearchForm;
import com.southwind.vo.PageVO;

public interface MoveoutService extends IService<Moveout> {
    public PageVO list(Integer page, Integer size);
    public PageVO search(SearchForm searchForm);
    public Boolean moveout(Integer id,String reason);
    public PageVO moveoutList(Integer page, Integer size);
    public PageVO moveoutSearch(SearchForm searchForm);
}
