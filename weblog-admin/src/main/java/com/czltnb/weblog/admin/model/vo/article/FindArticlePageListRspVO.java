package com.czltnb.weblog.admin.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindArticlePageListRspVO {

    /**
     * 文章ID
     */
    private Long id;

    private String title;


    private String cover;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;
}
