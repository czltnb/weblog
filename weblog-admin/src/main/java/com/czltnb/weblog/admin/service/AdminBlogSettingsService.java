package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.czltnb.weblog.common.utils.Response;

public interface AdminBlogSettingsService {

    /**
     * 修改博客设置信息
     */
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /**
     * 获取博客设置详情信息
     */
    Response findBlogSettingsDetail();
}
