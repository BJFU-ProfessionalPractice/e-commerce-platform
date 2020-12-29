package com.example.demo.service;


import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.pojo.Address;
import com.example.demo.pojo.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: zzw
 * @Date: 2020-12-28 21:26
 * @Description:
 */
@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public List<Address> getAllAddress(String userId){
        Address address=new Address();
        address.setShopuserId(Integer.parseInt(userId));
        List<Address> list=addressMapper.select(address);
        return list;
    }

    public void deleteAddressById(String addressId){
        Address address=new Address();
        address.setIsDelete(1);

        Example example=new Example(ShoppingCart.class);
        example.and()
                .andEqualTo("id",Integer.parseInt(addressId));
        try {
            addressMapper.updateByExampleSelective(address, example);
        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.DELETE_ADDRESS_FAIL);
        }
    }

}
