package cn.itcast.cronJobs;

import cn.itcast.constant.RedisConstant;
import cn.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        Set<String> uselessPics = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (uselessPics != null) {
            for (String uselessPic : uselessPics) {
                QiniuUtils.deleteFileFromQiniu(uselessPic);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,uselessPic);
                System.out.println(uselessPic);
            }
        }
    }
}