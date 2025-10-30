package com.czltnb.weblog.common.domain.dos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDO {

    private Long id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Byte isDeleted;
}
