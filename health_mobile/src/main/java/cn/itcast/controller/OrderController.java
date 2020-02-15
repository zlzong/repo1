package cn.itcast.controller;


import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderService;
import cn.itcast.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import sun.rmi.runtime.NewThreadAction;

import java.util.Map;

@RestController
@RequestMapping("/orderController")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit (@RequestBody Map map) {
        //获取验证码比对
        String validateCode = (String) map.get("validateCode");
        String validateCodeInRedis = jedisPool.getResource().get(map.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER);
        //比对成功，预约
        if (validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            try {
                String submitId = orderService.submit(map);
                if (submitId == null) {
                    return new Result(false, "预约失败");
                } else {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, (String) map.get("telephone"),(String) map.get("orderDate"));
                    return new Result(true, MessageConstant.ORDER_SUCCESS,submitId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,"网络异常，请稍后尝试");
            }
        } else {
            //验证码校验失败失败，返回数据
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }


    @RequestMapping("/findById")
    public Result findById(String id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}