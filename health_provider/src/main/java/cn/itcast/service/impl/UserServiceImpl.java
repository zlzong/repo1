package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.dao.UserDao;
import cn.itcast.pojo.User;
import cn.itcast.service.OrderService;
import cn.itcast.service.UserService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

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
        //今日新增会员
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //会员总数
        Integer totalMember = memberDao.findMemberTotalCount();
        //本周新增会员
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(mondayDate);
        //本月新增会员
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //今日预约人数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //本周预约人数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(mondayDate);
        //本月预约人数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //今日到诊人数
        Integer todayVisitNumber = orderDao.findVisitsCountByDate(today);
        //本周到诊人数
        Integer thisWeekVisitNumber = orderDao.findVisitsCountAfterDate(mondayDate);
        //本月到诊人数
        Integer thisMonthVisitNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        Map<String,Object> result = new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitNumber);
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}