package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.CategoryDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryDOMapper {

    CategoryDO selectByName(@Param("name") String categoryName);

    CategoryDO selectById(@Param("id") Long id);

    List<CategoryDO> selectAll();

    int insertCategory(CategoryDO category);

    //模糊查询某个分类的文章数
    //SQL使用了拼接函数 concat
    int selectCountByCategoryName(String categoryName);


    //只要 SQL 能查询出多条记录，且 resultMap（或 resultType）指向单个实体类（如 CategoryDO），
    // 同时 Mapper 接口方法返回值声明为 List<实体类>（如 List<CategoryDO>），MyBatis 就会自动将结果封装为集合返回，
    List<CategoryDO> selectPageListByCategoryName(
            @Param("name") String categoryName,
            @Param("offset") long offset,
            @Param("pageSize")long pageSize
    );

    //逻辑删除，修改分类的 is_deleted字段
    int deleteCategoryById(@Param("id") Long id);
}
