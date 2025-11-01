package com.czltnb.weblog.web.service.impl;

import com.czltnb.weblog.common.domain.dos.BlogSettingsDO;
import com.czltnb.weblog.common.domain.mapper.BlogSettingsDOMapper;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import com.czltnb.weblog.web.service.BlogSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlogSettingsServiceImpl implements BlogSettingsService {

    @Autowired
    private BlogSettingsDOMapper blogSettingsDOMapper;

    /**
     * 获取博客设置信息
     *
     * @return
     */
    @Override
    public Response findDetail() {
        // 查询博客设置信息（约定的 ID 为 1）
        BlogSettingsDO blogSettingsDO = blogSettingsDOMapper.selectById(1L);
        // DO 转 VO
        FindBlogSettingsDetailRspVO findBlogSettingsDetailRspVO = FindBlogSettingsDetailRspVO.builder()
                .logo(blogSettingsDO.getLogo())
                .name(blogSettingsDO.getName())
                .avatar(blogSettingsDO.getAvatar())
                .author(blogSettingsDO.getAuthor())
                .introduction(blogSettingsDO.getIntroduction())
                .githubHomepage(blogSettingsDO.getGithubHomepage())
                .csdnHomepage(blogSettingsDO.getCsdnHomepage())
                .giteeHomepage(blogSettingsDO.getGiteeHomepage())
                .zhihuHomepage(blogSettingsDO.getZhihuHomepage())
                .build();
        return Response.success(findBlogSettingsDetailRspVO);
    }
}
