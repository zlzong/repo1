package cn.itcast.service;

import cn.itcast.pojo.Member;

public interface LoginService {

    public Member checkMember(String telephone);
    public void addMember(Member member);
}