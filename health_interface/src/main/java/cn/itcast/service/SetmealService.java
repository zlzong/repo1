package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);
    PageResult findPage(QueryPageBean queryPageBean);
    Setmeal findById(Integer id);
    Integer[] findCheckGroupBySetmealId(Integer id);
    List<Setmeal> getSetmeal();
}