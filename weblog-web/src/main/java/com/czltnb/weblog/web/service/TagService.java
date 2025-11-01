package com.czltnb.weblog.web.service;

import com.czltnb.weblog.common.utils.Response;

public interface TagService {
    /**
     * 获取标签列表
     * @return
     */
    Response findTagList();
}
