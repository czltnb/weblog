package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.UpdateAdminUserPasswordReqVO;
import com.czltnb.weblog.common.utils.Response;

public interface AdminUserService {

    /**
     * 修改密码接口
     */
    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);

}

