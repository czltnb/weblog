package com.czltnb.weblog.admin.model.vo.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindTagPageListReqVO {

    private String tagName;

    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;
}
