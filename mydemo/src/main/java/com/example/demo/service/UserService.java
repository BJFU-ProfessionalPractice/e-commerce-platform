package com.example.demo.service;

import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:23
 * @Description:
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User login(User user) {
        user.setPassword(MD5Util.MD5Encode(user.getPassword() + "", "UTF-8"));
        User one = userMapper.selectOne(user);
        if (one == null) {
            throw new XmException(ExceptionEnum.GET_USER_NOT_FOUND);
        }
        return one;
    }

    public User getUserByName(String username){
        User one = new User();
        one.setUsername(username);

        Example example=new Example(User.class);
        example.selectProperties("userId","username").and().andEqualTo("username",username);
        List<User> userlist= userMapper.selectByExample(example);

        if(userlist.size()==1){
            return userlist.get(0);
        }else{
            return null;
        }
    }

    public void register(User user) {
        User one = new User();
        one.setUsername(user.getUsername());
        // 先去看看用户名是否重复
        if (userMapper.selectCount(one) == 1) {
            // 用户名已存在
            throw new XmException(ExceptionEnum.SAVE_USER_REUSE);
        }
        // 使用md5对密码进行加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword() + "", "UTF-8"));
        // 存入数据库
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.SAVE_USER_ERROR);
        }
    }

    public void isUserName(String username) {
        User one = new User();
        one.setUsername(username);
        // 先去看看用户名是否重复
        if (userMapper.selectCount(one) == 1) {
            // 用户名已存在
            throw new XmException(ExceptionEnum.SAVE_USER_REUSE);
        }
    }


}
