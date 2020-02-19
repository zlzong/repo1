package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    //添加检查项
    public void add(CheckItem checkItem);
    public PageResult findPage(QueryPageBean queryPageBean);
    public void deleteById(Integer id);
    CheckItem findById(Integer id);
    void edit(CheckItem checkItem);
    List<CheckItem> findAll();
}