package com.czltnb.weblog.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 通用下拉列表展示类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectListRspVO {

    /**
     * Select下拉展示的标签名，如分类名
     */
    private String label;

    /**
     * Select下拉列表的value值，如分类ID等
     */
    private Object value;
}
