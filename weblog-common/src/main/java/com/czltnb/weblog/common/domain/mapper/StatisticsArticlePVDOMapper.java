package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;

public interface StatisticsArticlePVDOMapper {

    int insert(StatisticsArticlePVDO articlePVDO);

    int increasePVCount(LocalDate currDate);
}
