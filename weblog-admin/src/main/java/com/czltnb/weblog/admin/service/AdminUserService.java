package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.czltnb.weblog.common.utils.Response;

public interface AdminUserService {

    /**
     * 修改密码接口
     */
    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);

    /**
     * 获取当前用户登录信息
     * @return
     */
    Response findUserInfo();
}

