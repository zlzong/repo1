package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.dao.UserDao;
import cn.itcast.pojo.User;
import cn.itcast.service.UserService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MemberDao memberDao;

    public User findByUserName(String username) {
        User userByQuery = userDao.findByUserName(username);
        return userByQuery;
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> monthsList) {
        List<Integer> list = new ArrayList<>();
        for (String month : monthsList) {
            month = month + ".31";
            Integer count = userDao.findMemberCountByMonth(month);
            list.add(count);
        }

        return list;
    }

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        String mondayDate = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        Integer memberTotalCount = memberDao.findMemberTotalCount();
        Integer memberCountAfterDate = memberDao.findMemberCountAfterDate(mondayDate);
        Integer memberCountAfterDate1 = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        return null;
    }
}