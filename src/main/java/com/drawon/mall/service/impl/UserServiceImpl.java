package com.drawon.mall.service.impl;
import com.drawon.mall.mapper.UserMapper;
import com.drawon.mall.entity.User;
import com.drawon.mall.service.PageService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;



/**
 * Created by CodeGenerator on 2020/03/28.
 */
@Slf4j
@Service
public class UserServiceImpl  extends PageService {


    @Autowired
    private UserMapper mapper;


    public User selectByPK(String key) {
        return mapper.selectByPrimaryKey(key);
    }


    public int save(User entity) {
        return mapper.insert(entity);
    }

    public int saveNotNull(User entity) {
        return mapper.insertSelective(entity);
    }

    public int delete(String key) {
        return mapper.deleteByPrimaryKey(key);
    }

    public int updateAll(User entity) {
      return mapper.updateByPrimaryKey(entity);
    }

    public int updateNotNull(User entity) {
    return mapper.updateByPrimaryKeySelective(entity);
    }

}
