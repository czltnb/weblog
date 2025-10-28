package com.czltnb.weblog.jwt.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.czltnb.weblog.common.domain.dos.UserDO;
import com.czltnb.weblog.common.domain.dos.UserRoleDO;
import com.czltnb.weblog.common.domain.mapper.UserDOMapper;
import com.czltnb.weblog.common.domain.mapper.UserRoleDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserRoleDOMapper userRoleDOMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询
        UserDO userDO = userDOMapper.findByUsername(username);

        if(Objects.isNull(userDO)){
            throw new UsernameNotFoundException("该用户不存在");
        }

        //用户角色(一个用户可能有很多角色，所以返回的是集合)
        List<UserRoleDO> roleDOS = userRoleDOMapper.selectByUsername(username);
        String[] roleArr = null;

        //List转数组
        if (!CollectionUtils.isEmpty(roleDOS)) {
            List<String> roles = roleDOS.stream().map(p-> p.getRole()).collect(Collectors.toList());
            roleArr = roles.toArray(new String[roles.size()]);
        }

        // authorities 用于指定角色，这里写死为 ADMIN 管理员
        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(roleArr)
                .build();
    }
}
