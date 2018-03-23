package com.real.userserver.user.dao;

import com.real.userserver.user.model.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_auth
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer userAuthId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_auth
     *
     * @mbg.generated
     */
    int insert(UserAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_auth
     *
     * @mbg.generated
     */
    int insertSelective(UserAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_auth
     *
     * @mbg.generated
     */
    UserAuth selectByPrimaryKey(Integer userAuthId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_auth
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserAuth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_auth
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserAuth record);
}