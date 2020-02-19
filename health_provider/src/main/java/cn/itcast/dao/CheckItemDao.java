package cn.itcast.dao;


import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckItemDao {
    //添加检查项
    public void add(CheckItem checkItem);
    public Page<CheckItem> selectByCondition(String queryString);
    public long findCountByCheckItemId(Integer id);
    public void deleteById(Integer id);
    public CheckItem findById(Integer id);
    void edit(CheckItem checkItem);
    List<CheckItem> findAll();
}