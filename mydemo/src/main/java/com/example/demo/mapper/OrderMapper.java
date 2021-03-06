package com.example.demo.mapper;

import com.example.demo.pojo.Order;
import com.example.demo.vo.OrderVo;
import com.example.demo.vo.OrderVoVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {

    @Select("select `order`.*, product.product_name as productName, product.product_picture as productPicture " +
            "from `order`, product where `order`.product_id = product.product_id and `order`.user_id = #{userId}")
    List<OrderVo> getOrderVoByUserId(Integer userId);



    @Select("select `order`.order_time as time,`order`.order_id as orderId,`order`.state as state,`order`.product_num as productNum,`order`.product_price as productPrice,address.people as people \n" +
            "from `order`, address where `order`.address_id = address.id and `order`.user_id = #{userId} \n" +
            "group by `order`.order_id")
    List<OrderVoVo> getOrderVoVoByUserId(Integer userId);
}
