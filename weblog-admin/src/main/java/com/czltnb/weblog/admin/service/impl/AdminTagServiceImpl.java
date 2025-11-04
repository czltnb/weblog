package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.tag.AddTagReqVO;
import com.czltnb.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.czltnb.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.czltnb.weblog.admin.model.vo.tag.FindTagPageListRspVO;
import com.czltnb.weblog.admin.service.AdminTagService;
import com.czltnb.weblog.common.domain.dos.ArticleTagRelDO;
import com.czltnb.weblog.common.domain.dos.TagDO;
import com.czltnb.weblog.common.domain.mapper.ArticleTagRelDOMapper;
import com.czltnb.weblog.common.domain.mapper.TagDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.model.vo.SelectListRspVO;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminTagServiceImpl implements AdminTagService {

    @Autowired
    private TagDOMapper tagDOMapper;

    @Autowired
    private ArticleTagRelDOMapper articleTagRelDOMapper;

    @Override
    public Response addTag(AddTagReqVO addTagReqVO) {
//        String tagName = addTagReqVO.getName();

//        TagDO tagDO = tagDOMapper.selectByName(tagName);
//
//        if(Objects.nonNull(tagDO)){
//            log.warn("标签名称: {}，此标签已经存在，无法重复创建",tagName);
//            throw new BizException(ResponseCodeEnum.TAG_ID_IS_NOT_EXISTED);
//        }
//
//        TagDO insertTagDO = TagDO.builder()
//                .name(tagName)
//                .build();
//
//        tagDOMapper.insertTag(insertTagDO);

        List<String> tags = addTagReqVO.getTags();
        List<TagDO> tagDOS = tagDOMapper.selectByTagsName(tags);
        if(!CollectionUtils.isEmpty(tagDOS)) {
            throw new BizException(ResponseCodeEnum.TAG_NAME_IS_EXISTED);
        }

        List<TagDO> insertTagDOS = tags.stream().map(tag ->
                TagDO.builder()
                    .name(tag)
                    .build()).collect(Collectors.toList());
        tagDOMapper.insertTags(insertTagDOS);

        return Response.success();
    }

    @Override
    public PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO){
        String tagName = findTagPageListReqVO.getName();
        long pageNo = findTagPageListReqVO.getPageNo();

        long pageSize = findTagPageListReqVO.getPageSize();

        long totalCount = tagDOMapper.selectCountByTagName(tagName);
        if(totalCount == 0){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        long offset = PageResponse.getOffset(pageNo, pageSize);

        LocalDate startDate = findTagPageListReqVO.getStartDate();
        LocalDate endDate = findTagPageListReqVO.getEndDate();


//        List<TagDO> tagDOS = tagDOMapper.selectPageListByTagName(tagName,offset,pageSize);
        List<TagDO> tagDOS = tagDOMapper.selectPageListByTagNameInTargetTime(tagName,offset,pageSize,startDate,endDate);
        if(CollectionUtils.isEmpty(tagDOS)){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        List<FindTagPageListRspVO> findTagPageListRspVOS = null;
        findTagPageListRspVOS = tagDOS.stream()
                .map(tagDO -> FindTagPageListRspVO.builder()
                        .id(tagDO.getId())
                        .name(tagDO.getName())
                        .createTime(tagDO.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.success(findTagPageListRspVOS,pageNo,totalCount,pageSize);
    }

    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        Long tagId = deleteTagReqVO.getId();

        TagDO tagDO = tagDOMapper.selectById(tagId);
        if(Objects.isNull(tagDO)){
            throw new BizException(ResponseCodeEnum.TAG_ID_IS_NOT_EXISTED);
        }

        // 校验该标签下是否有关联的文章，若有，则不允许删除，提示用户需要先删除标签下的文章
        ArticleTagRelDO articleTagRelDO = articleTagRelDOMapper.selectOneByTagId(tagId);

        if (Objects.nonNull(articleTagRelDO)) {
            log.warn("==> 此标签下包含文章，无法删除，tagId: {}", tagId);
            throw new BizException(ResponseCodeEnum.TAG_CAN_NOT_DELETE);
        }

        //逻辑删除
        tagDOMapper.deleteTagById(tagId);

        return Response.success();
    }

    @Override
    public Response selectTagList(){

        List<TagDO> tagDOS = tagDOMapper.selectAll();
        //DO转VO
        List<SelectListRspVO> selectListTagRspVOS = null;

        //如果标签的数据不为空
        if(!CollectionUtils.isEmpty(tagDOS)){
            selectListTagRspVOS = tagDOS.stream()
                    .map(tagDO -> SelectListRspVO.builder()
                            .label(tagDO.getName())
                            .value(tagDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(selectListTagRspVOS);
    }
}
