package com.czltnb.weblog.admin.schedule;

import com.czltnb.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.czltnb.weblog.common.domain.mapper.StatisticsArticlePVDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class InitPVRecordScheduledTask {

    @Autowired
    private StatisticsArticlePVDOMapper articlePVDOMapper;

    @Scheduled(cron = "0 0 23 * * ?") //每天晚间 23 点执行
    public void execute() {
        // 定时任务执行的业务逻辑
        log.info("==> 开始执行初始化明日 PV 访问量记录定时任务");

        // 当日日期
        LocalDate currDate = LocalDate.now();

        // 明日
        LocalDate tomorrowDate = currDate.plusDays(1);

        //组装插入的记录
        StatisticsArticlePVDO articlePVDO = StatisticsArticlePVDO.builder()
                .pvDate(tomorrowDate)
                .pvCount(0L) //默认阅读量 0L
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        articlePVDOMapper.insert(articlePVDO);

        log.info("==> 结束执行初始化明日 PV 访问量记录定时任务");

    }
}
