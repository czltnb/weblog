package com.czltnb.weblog.admin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCategoryReqVO {

    @NotNull(message = "文章分类 id 不能为空")
    private Long id;
}
