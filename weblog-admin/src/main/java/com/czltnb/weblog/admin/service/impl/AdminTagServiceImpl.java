package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.tag.AddTagReqVO;
import com.czltnb.weblog.admin.service.AdminTagService;
import com.czltnb.weblog.common.domain.dos.TagDO;
import com.czltnb.weblog.common.domain.mapper.TagDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class AdminTagServiceImpl implements AdminTagService {

    @Autowired
    private TagDOMapper tagDOMapper;

    @Override
    public Response addTag(AddTagReqVO addTagReqVO) {
        String tagName = addTagReqVO.getName();

        TagDO tagDO = tagDOMapper.selectByName(tagName);

        if(Objects.nonNull(tagDO)){
            log.warn("标签名称: {}，此标签已经存在，无法重复创建",tagName);
            throw new BizException(ResponseCodeEnum.TAG_ID_IS_NOT_EXISTED);
        }

        TagDO insertTagDO = TagDO.builder()
                .name(tagName)
                .build();

        tagDOMapper.insertTag(insertTagDO);
        return Response.success();
    }
}
