package com.example.demo.vo;

import com.example.demo.pojo.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: wdd
 * @Date: 2020-03-27 16:29
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderVo extends Order {

    private String productName;

    private String productPicture;

}
