package cn.itcast.service.impl;

import cn.itcast.constant.RedisConstant;
import cn.itcast.dao.SetmealDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;

    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        Integer setmealId = setmeal.getId();
        String imgFileName = setmeal.getImg();
        Setmeal quriedSetmeal = setmealDao.findById(setmealId);

        if (quriedSetmeal != null) {
            setmealDao.updateSetmeal(setmeal);
            String quriedSetmealImg = quriedSetmeal.getImg();
            setmealDao.deleteDataBySetmealId(setmealId);
            this.addSetmealAndCheckGroup(setmealId,checkGroupIds);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,imgFileName);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,quriedSetmealImg);
        } else {
            setmealDao.add(setmeal);
            this.addSetmealAndCheckGroup(setmeal.getId(),checkGroupIds);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,imgFileName);
        }

        this.generateMobileStaticHtml();
    }


    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();
        return new PageResult(total,rows);
    }


    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }


    public Integer[] findCheckGroupBySetmealId(Integer id) {
        Integer[] checkGroupIds = setmealDao.findCheckGroupBySetmealId(id);
        return checkGroupIds;
    }


    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }


    public void addSetmealAndCheckGroup(Integer setmealId, Integer[] checkGroupIds) {
        for (Integer checkGroupId : checkGroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",setmealId);
            map.put("checkGroupId",checkGroupId);
            setmealDao.addSetmealAndCheckGroup(map);
        }
    }

    //生成静态页面
    public void generateHtml(String templateName,String htmlPageName,Map map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            template.process(map,out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateMobileStaticHtml() {
        //查询数据
        List<Setmeal> setmeal = setmealDao.getSetmeal();
        //列表页面
        this.generateMobileSetmealHtml(setmeal);
        //详情套餐
        this.generateMobileSetmealDetailHtml(setmeal);
    }


    public void generateMobileSetmealHtml(List<Setmeal> setmeal) {
        Map map = new HashMap();
        map.put("setmealList",setmeal);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Integer id = setmeal.getId();
            Setmeal setmeal1 = setmealDao.findById(id);
            Map map = new HashMap();
            map.put("setmeal",setmeal1);
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId() + ".html",map);
        }
    }
}