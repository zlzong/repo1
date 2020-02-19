package cn.itcast.service;

import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.pojo.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询用户
        User userByQuery = userService.findByUserName(username);
        if (userByQuery == null) {
            return null;
        }

        //查询角色权限信息,并且将权限添加到指定list集合中
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<Role> roles = userByQuery.getRoles();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, userByQuery.getPassword(), grantedAuthorities);
        return user;
    }
}
