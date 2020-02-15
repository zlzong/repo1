package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.pojo.Member;
import cn.itcast.service.LoginService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = LoginService.class)
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 有bug
     * @param telephone
     * @return
     * Expected one result (or null) to be returned by selectOne(), but found: 2
     */
    public Member checkMember(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void addMember(Member member) {
        memberDao.add(member);
    }
}
