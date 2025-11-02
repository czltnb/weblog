package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.common.utils.Response;

public interface AdminDashboardService {

    /**
     * 获取仪表盘基础统计信息
     * @return
     */
    Response findDashboardStatistics();
}
