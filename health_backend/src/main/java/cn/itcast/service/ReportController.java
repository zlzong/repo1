package cn.itcast.service;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private UserService userService;

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
        List<String> monthsList = new ArrayList<>();
        for (int i = 0;i < 12;i++) {
            calendar.add(Calendar.MONTH,1);
            monthsList.add(sdf.format(calendar.getTime()));
        }

        Map<String,Object> map = new HashMap<>();
        map.put("months",monthsList);

        try {
            List<Integer> countList = userService.findMemberCountByMonth(monthsList);
            map.put("memberCount",countList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
        Map<String,Object> map = new HashMap<>();
        map.put("setmealCount",setmealCount);

        List<String> setmealNames = new ArrayList<>();
        for (Map<String, Object> m : setmealCount) {
            String name = (String) m.get("name");
            setmealNames.add(name);
        }

        map.put("setmeslNames",setmealNames);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String,Object> map = userService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
















