package com.czltnb.weblog.common.utils;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> extends Response<List<T>>{

    private long pageNo; //当前页码

    private long totalCount; //总数据量

    private long pageSize; //每页数据量

    private long totalPage; //总页数



    public static <T> PageResponse<T> success(List<T> data,long pageNo,long totalCount,long pageSize){
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setSuccess(true);
        pageResponse.setPageNo(pageNo);
        pageResponse.setTotalCount(totalCount);
        pageResponse.setPageSize(pageSize);

        //根据总数据量和每页数据量，计算总页数
        long totalPage = getTotalPage(totalCount,pageSize);
        pageResponse.setTotalPage(totalPage);

        pageResponse.setData(data);
        return pageResponse;
    }


    /**
     * 获取总页数
     * @return
     */
    public static long getTotalPage(long totalCount,long pageSize) {
        return pageSize == 0 ? 0 : (totalCount + pageSize - 1) / pageSize;
    }

    /**
     * 计算分页查询的offset
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static long getOffset(long pageNo,long pageSize) {
        //如果页码小于 1,默认返回第一页的 offset
        if(pageNo < 1){
            pageNo = 1;
        }
        return (pageNo - 1) * pageSize;
    }

}
