package com.czltnb.weblog.common.domain.dos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsArticlePVDO {

    private Long id;

    private LocalDate pvDate;

    private Long pvCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
