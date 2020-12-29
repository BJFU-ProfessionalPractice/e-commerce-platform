package com.example.demo.vo;

import com.example.demo.pojo.Order;

import java.util.List;

public class OrderVoVo {
    private Address address;
    private String time;
    private Integer orderId;
    private Integer state;
    private Double totalPrice;
    private List<OrderVo> orderList;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderVo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderVo> orderList) {
        this.orderList = orderList;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }




}
