package cn.itcast.dao;

import cn.itcast.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);

    public void addSetmealAndCheckGroup(Map<String, Integer> map);

    public Page<Setmeal> selectByCondition(String queryString);

    public Setmeal findById(Integer id);

    public Integer[] findCheckGroupBySetmealId(Integer id);

    public void updateSetmeal(Setmeal setmeal);

    public void deleteDataBySetmealId(Integer setmealId);

    public List<Setmeal> getSetmeal();
}