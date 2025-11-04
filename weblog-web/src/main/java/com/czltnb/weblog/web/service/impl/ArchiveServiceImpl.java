package com.czltnb.weblog.web.service.impl;

import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.archive.FindArchiveArticlePageListReqVO;
import com.czltnb.weblog.web.model.vo.archive.FindArchiveArticlePageListRspVO;
import com.czltnb.weblog.web.model.vo.archive.FindArchiveArticleRspVO;
import com.czltnb.weblog.web.service.ArchiveService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ArticleDOMapper articleDOMapper;


    @Override
    public PageResponse findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO) {
        long pageNo = findArchiveArticlePageListReqVO.getPageNo();

        long pageSize = findArchiveArticlePageListReqVO.getPageSize();

        long totalCount =articleDOMapper.selectCount();

        if(totalCount == 0){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        long offset = PageResponse.getOffset(pageNo,pageSize);

        List<ArticleDO> articleDOS = articleDOMapper.selectArticlePageList(offset, pageSize);

        //再判定一次，因为可能有"分类总数不为空，但是指定查询的页码大于最大页码，查到空数据"的情况
        if(CollectionUtils.isEmpty(articleDOS)){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        List<FindArchiveArticlePageListRspVO> vos = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(articleDOS)){
            //DO转VO
            List<FindArchiveArticleRspVO> findArchiveArticleRspVOS = articleDOS.stream()
                    .map(articleDO -> FindArchiveArticleRspVO.builder()
                            .id(articleDO.getId())
                            .cover(articleDO.getCover())
                            .title(articleDO.getTitle())
                            .createDate(articleDO.getCreateTime().toLocalDate())  //createTime转成createDate
                            .createMonth(YearMonth.from(articleDO.getCreateTime())) //createTime转成createMonth
                            .build())
                    .collect(Collectors.toList());

            //按照创建的月份分组
            Map<YearMonth,List<FindArchiveArticleRspVO>> map = findArchiveArticleRspVOS.stream().collect(Collectors.groupingBy(FindArchiveArticleRspVO::getCreateMonth));

            //使用 TreeMap 按月份倒序排列
            Map<YearMonth,List<FindArchiveArticleRspVO>> sortedMap = new TreeMap<>(Collections.reverseOrder());
            sortedMap.putAll(map);

            //遍历排序后的 Map，将其转换为归档 VO
            sortedMap.forEach((k,v) -> vos.add(FindArchiveArticlePageListRspVO.builder().month(k).articles(v).build()));
        }


        return PageResponse.success(vos,pageNo,totalCount,pageSize);
    }
}
