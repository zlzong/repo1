package cn.itcast.dao;

import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.pojo.User;

import java.util.List;

public interface UserDao {
    public User findByUserName(String username);
    public Role queryRoleById(Integer id);
    public Permission queryPermissionById(Integer id);
    public Integer findMemberCountByMonth(String month);
}