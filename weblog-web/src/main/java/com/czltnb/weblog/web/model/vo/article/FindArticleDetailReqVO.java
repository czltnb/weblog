package com.czltnb.weblog.web.model.vo.article;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查看文章详情 VO")
public class FindArticleDetailReqVO {

    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;
}
