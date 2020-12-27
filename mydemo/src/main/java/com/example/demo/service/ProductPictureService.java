package com.example.demo.service;

import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.ProductPictureMapper;
import com.example.demo.pojo.Product;
import com.example.demo.pojo.ProductPicture;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:22
 * @Description:
 */
@Service
public class ProductPictureService {

    @Autowired
    private ProductPictureMapper productPictureMapper;

    public List<ProductPicture> getProductPictureByProductId(String productId) {
        ProductPicture picture = new ProductPicture();
        picture.setProductId(Integer.parseInt(productId));
        List<ProductPicture> list = null;
        try {
            list = productPictureMapper.select(picture);
            if (ArrayUtils.isEmpty(list.toArray())) {
                throw new XmException(ExceptionEnum.GET_PRODUCT_PICTURE_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_PRODUCT_PICTURE_ERROR);
        }
        return list;
    }
}
