package com.cb.cloud.demo.dao;


import com.cb.cloud.demo.entity.DemoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DemoDao {

    DemoEntity getById(Integer id);
}


