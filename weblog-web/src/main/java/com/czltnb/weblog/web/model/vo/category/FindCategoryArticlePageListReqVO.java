package com.czltnb.weblog.web.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryArticlePageListReqVO {

    /**
     * 分类 ID
     */
    @NotNull(message = "分类 ID 不能为空")
    private Long id;

    @NotNull(message = "页码不能为空")
    private Integer pageNo;

    @NotNull(message = "每页尺寸不能为空")
    private Integer pageSize;

}
