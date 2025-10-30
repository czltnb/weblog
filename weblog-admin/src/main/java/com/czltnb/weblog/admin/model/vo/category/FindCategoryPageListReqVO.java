package com.czltnb.weblog.admin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryPageListReqVO {

    /**
     * 分类名称(可以为空,为空的话就是全模糊查询)
     */
    private String name;

    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1; // 默认值为第一页

//    /**
//     * 创建的起始日期，方便按照日期查询
//     */
//    private LocalDate startDate;
//
//    /**
//     * 创建的结束日期
//     */
//    private LocalDate endDate;

}
