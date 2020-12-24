package com.example.demo.common.service.impl;

import com.example.demo.common.service.CommonService;
import com.example.demo.entity.UserTable;
import com.example.demo.mapper.UserTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
    @Autowired()
    UserTableMapper userTableMapper;

    @Override
    public UserTable userLogin(UserTable userTable) {
        return userTableMapper.searchUserTableByUserName(userTable.getUserName());
    }

    @Override
    @Transactional
    public int updateUser(UserTable userTable) {
        return userTableMapper.updateUserTableByUserID(userTable);
    }
}
