package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.UserDO;
import org.apache.ibatis.annotations.Param;

//public interface UserMapper extends BaseMapper<UserDO> {
//    default UserDO findByUsername(String username) {
//        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserDO::getUsername, username);
//        return selectOne(wrapper);
//    }
//}

public interface UserDOMapper {

    UserDO findByUsername(@Param("username") String username);

}
