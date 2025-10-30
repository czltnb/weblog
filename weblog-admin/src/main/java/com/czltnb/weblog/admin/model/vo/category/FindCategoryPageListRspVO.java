package com.czltnb.weblog.admin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryPageListRspVO {

    //分类id
    private Long id;

    //分类名称
    private String name;

    //创建时间
    private Date createTime;
}
