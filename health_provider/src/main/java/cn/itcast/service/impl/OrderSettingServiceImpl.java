package cn.itcast.service.impl;

import cn.itcast.dao.OrderSettingDao;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0 ) {
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }


    public List<Map> getOrderSettingByMonth(String date) {
        if (date.split("-")[1].length() == 1) {
            date = date.split("-")[0] + "-0" + date.split("-")[1];
        }
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(date);
        List<Map> result = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            result.add(map);
        }
        return result;
    }


    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        long countByOrderDate = orderSettingDao.findCountByOrderDate(orderDate);
        if (countByOrderDate > 0) {
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }
}