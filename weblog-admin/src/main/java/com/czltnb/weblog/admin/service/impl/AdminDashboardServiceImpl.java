package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.dashboard.FindDashboardPVStatisticsInfoRspVO;
import com.czltnb.weblog.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.czltnb.weblog.admin.service.AdminDashboardService;
import com.czltnb.weblog.common.constant.Constants;
import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.dos.ArticlePublishCountDO;
import com.czltnb.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.domain.mapper.CategoryDOMapper;
import com.czltnb.weblog.common.domain.mapper.TagDOMapper;
import com.czltnb.weblog.common.utils.Response;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 获取文章发布热点统计信息
     *
     * @return
     */
    @Override
    public Response findDashboardPublishArticleStatistics() {
        // 当前日期
        LocalDate currDate = LocalDate.now();

        // 当前日期倒退一年的日期
        LocalDate startDate = currDate.minusYears(1);

        // 查找这一年内，每日发布的文章数量
        List<ArticlePublishCountDO> articlePublishCountDOS = articleDOMapper.selectDateArticlePublishCount(startDate,currDate.plusDays(1));


        /**
         * 上述代码中， 首先我们拿到当前日期，并通过 minusYears(1) 方法对前期日减去一年，拿到去年今日的日期，接着执行 SQL 查询这一整年的统计数据，
         * 若数据不为空，先对查询出的结果执行转 Map 操作， key 为日期, value 为文章发布数，
         * 接着，从去年今日开始循环，循环到今天, 一整年的数据，设置每天对应的发布数量，放到 map 集合中，最终返回到前端。
         */

        Map<LocalDate,Long> map = null;
        if(!CollectionUtils.isEmpty(articlePublishCountDOS)) {
            //DO转Map
            Map<LocalDate,Long> dateArticleCountMap = articlePublishCountDOS.stream()
                    .collect(Collectors.toMap(ArticlePublishCountDO::getDate,ArticlePublishCountDO::getCount));

            //有序Map，返回的日期文章数需要以升序排列
            map = Maps.newLinkedHashMap();
            // 从上一年的今天循环到今天
            for(; startDate.isBefore(currDate) || startDate.isEqual(currDate); startDate = startDate.plusDays(1)) {
                // 以日期作为 key 从dateArticleCountMap中取出文章发布总量
                Long count = dateArticleCountMap.get(startDate);
                //设置到返参 Map
                map.put(startDate,Objects.isNull(count) ? 0 : count);

            }
        }

        return Response.success(map);
    }

    /**
     * 获取文章最近一周 PV 访问量统计信息
     *
     * @return
     */
    @Override
    public Response findDashboardPVStatistics() {
        //查询最近一周的 PV 访问量记录
        List<StatisticsArticlePVDO> articlePVDOS = articleDOMapper.selectLatestWeekRecords();

        Map<LocalDate,Long> pvDateCountMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(articlePVDOS)) {
            //转 Map，方便后续通过日期获取 PV 访问量
            pvDateCountMap = articlePVDOS.stream().collect(Collectors.toMap(StatisticsArticlePVDO::getPvDate,StatisticsArticlePVDO::getPvCount));
        }

        FindDashboardPVStatisticsInfoRspVO vo = null;

        //日期集合
        List<String> pvDates = Lists.newArrayList();
        //PV集合
        List<Long> pvCounts = Lists.newArrayList();

        //当前日期
        LocalDate currDate = LocalDate.now();
        //一周前
        LocalDate tmpDate = currDate.minusWeeks(1);
        // 从一周前开始循环
        for (; tmpDate.isBefore(currDate) || tmpDate.isEqual(currDate); tmpDate = tmpDate.plusDays(1)) {
            // 设置对应日期的 PV 访问量
            pvDates.add(tmpDate.format(Constants.MONTH_DAY_FORMATTER));
            Long pvCount = pvDateCountMap.get(tmpDate);
            pvCounts.add(Objects.isNull(pvCount) ? 0 : pvCount);
        }

        vo = FindDashboardPVStatisticsInfoRspVO.builder()
                .pvDates(pvDates)
                .pvCounts(pvCounts)
                .build();

        return Response.success(vo);

    }
}
