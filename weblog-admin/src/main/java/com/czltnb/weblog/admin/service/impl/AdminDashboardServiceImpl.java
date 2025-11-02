package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.czltnb.weblog.admin.service.AdminDashboardService;
import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.domain.mapper.CategoryDOMapper;
import com.czltnb.weblog.common.domain.mapper.TagDOMapper;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private CategoryDOMapper categoryDOMapper;
    @Autowired
    private TagDOMapper tagDOMapper;


    /**
     * 获取仪表盘基础统计信息
     *
     * @return
     */
    @Override
    public Response findDashboardStatistics() {

        //查询文章总数
        Long articleTotalCount = Long.valueOf(articleDOMapper.selectCount());

        //查询分类总数
        Long categoryTotalCount = Long.valueOf(categoryDOMapper.selectCount());

        //查询标签总数
        Long tagTotalCount = Long.valueOf(tagDOMapper.selectCount());

        //总浏览量
        List<ArticleDO> articleDOS = articleDOMapper.selectAllReadNum();
        Long pvTotalCount = 0L;

        if(!CollectionUtils.isEmpty(articleDOS)) {
            //所有read_num相加
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        //组装 VO 类
        FindDashboardStatisticsInfoRspVO vo = FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build();

        return Response.success(vo);
    }
}
