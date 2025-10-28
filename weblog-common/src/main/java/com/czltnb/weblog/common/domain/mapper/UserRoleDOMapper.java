package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.UserRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//public interface UserRoleMapper extends BaseMapper<UserRoleDO> {
//
//    default List<UserRoleDO> selectByUsername(String username) {
//        LambdaQueryWrapper<UserRoleDO> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserRoleDO::getUsername,username);
//        return selectList(wrapper);
//    }
//
//}
public interface UserRoleDOMapper {

    /**
     * 返回用户角色集合(一个用户可能有很多角色，所以返回的是集合)
     * @param username
     * @return
     */
    List<UserRoleDO> selectByUsername(@Param("username") String username);

}

