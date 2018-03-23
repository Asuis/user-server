package com.real.userserver.user.mapper;

import com.qcloud.weapp.authorization.UserInfo;
import com.real.userserver.user.dto.OurUserInfo;
import com.real.userserver.user.model.UserDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author asuis
 */
@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserInfoMapper USER_INFO_MAPPER = Mappers.getMapper(UserInfoMapper.class);

    /**
     * @param userInfo wafer sdk得到的userinfo
     * @return UserDetail 类型
     * */
    @Mappings({
            @Mapping(source = "nickName", target = "nickName"),
            @Mapping(source = "avatarUrl",target = "avatarUrl"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "language", target = "language"),
            @Mapping(source = "city", target = "city"),
            @Mapping(source = "province", target = "province"),
            @Mapping(source = "country", target = "country")
    })
    UserDetail userInfoToUserDetail(UserInfo userInfo);
    @Mappings({
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "nickName", target = "nickName"),
            @Mapping(source = "avatarUrl",target = "avatarUrl"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "language", target = "language"),
            @Mapping(source = "city", target = "city"),
            @Mapping(source = "province", target = "province"),
            @Mapping(source = "country", target = "country")
    })
    OurUserInfo userDetailToOurUserInfo(UserDetail userDetail);
}
