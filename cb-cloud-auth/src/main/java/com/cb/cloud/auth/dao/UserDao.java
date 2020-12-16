package com.cb.cloud.auth.dao;

import com.cb.cloud.auth.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    List<UserEntity> findByUsername(String userName);
}
