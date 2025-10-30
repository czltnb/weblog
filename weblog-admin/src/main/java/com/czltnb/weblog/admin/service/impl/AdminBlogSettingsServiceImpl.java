package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.czltnb.weblog.admin.service.AdminBlogSettingsService;
import com.czltnb.weblog.common.domain.dos.BlogSettingsDO;
import com.czltnb.weblog.common.domain.mapper.BlogSettingsDOMapper;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class AdminBlogSettingsServiceImpl implements AdminBlogSettingsService {

    @Autowired
    private BlogSettingsDOMapper blogSettingsDOMapper;

    @Override
    public Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        //VO转DO
        BlogSettingsDO blogSettingsDO = BlogSettingsDO.builder()
                .id(1L)
                .logo(updateBlogSettingsReqVO.getLogo())
                .name(updateBlogSettingsReqVO.getName())
                .author(updateBlogSettingsReqVO.getAuthor())
                .introduction(updateBlogSettingsReqVO.getIntroduction())
                .avatar(updateBlogSettingsReqVO.getAvatar())
                .githubHomepage(updateBlogSettingsReqVO.getGithubHomepage())
                .giteeHomepage(updateBlogSettingsReqVO.getGiteeHomepage())
                .csdnHomepage(updateBlogSettingsReqVO.getCsdnHomepage())
                .zhihuHomepage(updateBlogSettingsReqVO.getZhihuHomepage())
                .build();

        //先看看数据库中是否存在id为1L的用户
        BlogSettingsDO blogSettingsDO1 = blogSettingsDOMapper.selectById(1L);
        //存在就更新
        if(Objects.nonNull(blogSettingsDO1)) {
            blogSettingsDOMapper.update(blogSettingsDO1);
        } else{
            blogSettingsDOMapper.insert(blogSettingsDO);
        }

        return Response.success();
    }
}
