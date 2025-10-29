package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.user.FindUserInfoRspVO;
import com.czltnb.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.czltnb.weblog.admin.service.AdminUserService;
import com.czltnb.weblog.common.domain.mapper.UserDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO){
        String username = updateAdminUserPasswordReqVO.getUsername();
        String password = updateAdminUserPasswordReqVO.getPassword();

        String encodePassword = passwordEncoder.encode(password);
        int count = userDOMapper.updatePasswordByUsername(username, encodePassword);
        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.USERNAME_NOT_FOUND);
    }

    /**
     * 直接获取存储在ThreadLocal中的信息,获取可以在用户登录时，把用户信息以Json的格式传入自定义的ThreadLocal中，包括用户头像什么的
     * @return
     */
    @Override
    public Response findUserInfo(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //拿到用户名
        String username = authentication.getName();

        FindUserInfoRspVO findUserInfoRspVO = FindUserInfoRspVO.builder()
                .username(username)
                .build();
        return Response.success(findUserInfoRspVO);
    }
}
