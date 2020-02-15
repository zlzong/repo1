package cn.itcast.service;

import java.util.Map;

public interface OrderService {
    public String submit(Map map) throws Exception;

    public Map findById(String id);
}
