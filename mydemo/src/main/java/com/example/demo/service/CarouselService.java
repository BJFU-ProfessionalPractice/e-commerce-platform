package com.example.demo.service;

import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.CarouselMapper;
import com.example.demo.pojo.Carousel;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:16
 * @Description:
 */
@Service
public class CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    public List<Carousel> getCarouselList() {
        List<Carousel> list = null;
        try {
            list = carouselMapper.selectAll();
            if (ArrayUtils.isEmpty(list.toArray())) {
                throw new XmException(ExceptionEnum.GET_CAROUSEL_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_CAROUSEL_ERROR);
        }
        return list;
    }

}
