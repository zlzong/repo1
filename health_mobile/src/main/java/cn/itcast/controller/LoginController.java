package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.LoginService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private LoginService loginService;

    @RequestMapping("/check")
    public Result check (@RequestBody Map map, HttpServletResponse response) throws Exception {
        //读取map中的验证码
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //对比map中的验证码和redis中储存的验证码是否一致
        if (validateCode != null && validateCode.equals(validateCodeInRedis)) {
            Member member = loginService.checkMember(telephone);
            //检查该会员是否注册，没有则创建会员，并且加入注册日期
            Member regMember;
            if (member == null) {
                regMember = new Member(telephone, new Date());
                loginService.addMember(regMember);
            }
            //将会员信息加密后存入cookie
            Cookie cookie = new Cookie("userTelephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(30*24*60*60);
            response.addCookie(cookie);
            //将会员信息序列化后存入redis
            String memberJson = JSONObject.toJSONString(member);
            jedisPool.getResource().setex("userTelephone",7*24*60*60,memberJson);
        } else {
            return new Result(false, "登录失败");
        }

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}