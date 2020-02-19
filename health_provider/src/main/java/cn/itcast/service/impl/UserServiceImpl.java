package cn.itcast.service.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.pojo.User;
import cn.itcast.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public User findByUserName(String username) {
        User userByQuery = userDao.findByUserName(username);
        return userByQuery;
    }
}
