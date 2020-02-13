package cn.itcast.dao;

import cn.itcast.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.Map;

public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);
    public void setCheckGroupAndCheckItem(Map map);
    Page<CheckGroup> findByCondition(String queryString);
    CheckGroup findById(Integer id);
    Integer[] findCheckItemByCheckGroupId(Integer id);
    void edit(CheckGroup checkGroup);
    void deleteAssociation(Integer id);
    void deleteById(Integer id);
}