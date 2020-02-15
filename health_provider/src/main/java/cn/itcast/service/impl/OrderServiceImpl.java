package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.dao.OrderSettingDao;
import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    public String submit(Map map) throws Exception {
        String orderDate = (String) map.get("orderDate");
        //判断当天预约是否开放
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByDate(orderDate);
        if (orderSetting == null) {
            return null;
        }

        //判断预约是否满额
        int reservations = orderSetting.getReservations();
        int number = orderSetting.getNumber();
        if (reservations >= number) {
            return null;
        }

        //判断是否重复预约
        String telephone = (String) map.get("telephone");

        /**
         * bug:
         * memberDao
         * Expected one result (or null) to be returned by selectOne(), but found: 2
         */
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            Integer memberId = member.getId();
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐ID
            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0){
                return null;
            }
        }
        //判断是否会员
        member = new Member();
        member.setName((String) map.get("name"));
        member.setPhoneNumber(telephone);
        member.setIdCard((String) map.get("idCard"));
        member.setSex((String) map.get("sex"));
        member.setRegTime(new Date());
        memberDao.add(member);//自动完成会员注册
        //生成预约数据
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐ID
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //修改预约名额
        //返回预约id
        return order.getId().toString();
    }

    @Override
    public Map findById(String id) {
       return orderDao.findById(id);
    }
}
