package com.example.demo.service;

import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getAll() {
        List<Category> categories = null;
        try {
            categories = categoryMapper.selectAll();
            if (categories == null) {
                throw new XmException(ExceptionEnum.GET_CATEGORY_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_CATEGORY_ERROR);
        }
        return categories;
    }
}
