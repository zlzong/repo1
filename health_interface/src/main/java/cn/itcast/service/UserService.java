package cn.itcast.service;

import cn.itcast.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public User findByUserName(String username);

    public List<Integer> findMemberCountByMonth(List<String> monthsList);

    public Map<String, Object> getBusinessReportData() throws Exception;
}