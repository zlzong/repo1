package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.utils.SMSUtils;
import cn.itcast.utils.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCode {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成验证码
        String validateCode = ValidateCodeUtils.generateValidateCode4String(4);
        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        //将验证码存入redis
        System.out.println("预约验证码为:" + validateCode);
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,300,validateCode);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //生成验证码
        String validateCode = ValidateCodeUtils.generateValidateCode4String(4);
        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,validateCode);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        //将验证码存入redis
        System.out.println("登录验证码为:" + validateCode);
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}