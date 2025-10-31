package com.czltnb.weblog.admin.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindArticleDetailRspVO {

    private Long id;

    private String title;

    private String cover;

    private String summary;

    private String content;

    private Long categoryId;

    private List<Long> tagIds;

}
