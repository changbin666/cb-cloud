package com.cb.cloud.demo.service.impl;

import com.cb.cloud.demo.dao.DemoDao;
import com.cb.cloud.demo.entity.DemoEntity;
import com.cb.cloud.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DemoServiceimpl implements DemoService {

    @Autowired
    public DemoDao demoDao;

    @Override
    public DemoEntity getById(Integer id) {
        return demoDao.getById(id);
    }
}

