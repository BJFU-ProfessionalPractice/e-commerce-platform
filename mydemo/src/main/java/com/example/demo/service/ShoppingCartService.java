package com.example.demo.service;

import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.pojo.Product;
import com.example.demo.pojo.ShoppingCart;
import com.example.demo.pojo.User;
import com.example.demo.vo.CartVo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:22
 * @Description:
 */
@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    public List<CartVo> getCartByUserId(String userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(Integer.parseInt(userId));
        List<ShoppingCart> list = null;
        List<CartVo> cartVoList = new ArrayList<>();
        try {
            list = cartMapper.select(cart);
            for (ShoppingCart c : list) {
                System.out.println(c.getIsCheck()==1);
                cartVoList.add(getCartVo(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_CART_ERROR);
        }
        return cartVoList;
    }


    @Transactional
    public CartVo addCart(String productId, String userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(Integer.parseInt(userId));
        cart.setProductId(Integer.parseInt(productId));
        // 查看数据库是否已存在,存在数量直接加1
        ShoppingCart one = cartMapper.selectOne(cart);
        if (one != null) {
            // 还要判断是否达到该商品规定上限
            if (one.getNum() >= 5) { // TODO 这里默认设为5 后期再动态修改
                throw new XmException(ExceptionEnum.ADD_CART_NUM_UPPER);
            }
            one.setNum(one.getNum() + 1);
            cartMapper.updateByPrimaryKey(one);
            return null;
        }else {
            // 不存在
            cart.setNum(1);
            cartMapper.insert(cart);
            return getCartVo(cart);
        }
    }

    /**
     * 封装类
     * @param cart
     * @return
     */
    private CartVo getCartVo(ShoppingCart cart) {
        // 获取商品，用于封装下面的类
        Product product = productMapper.selectByPrimaryKey(cart.getProductId());
        // 返回购物车详情
        CartVo cartVo = new CartVo();
        cartVo.setId(cart.getId());
        cartVo.setProductId(cart.getProductId());
        cartVo.setProductName(product.getProductName());
        cartVo.setProductImg(product.getProductPicture());
        cartVo.setPrice(product.getProductSellingPrice());
        cartVo.setNum(cart.getNum());
        cartVo.setMaxNum(5); // TODO 这里默认设为5 后期再动态修改
//        System.out.println(cart.getIsCheck()==1);
        cartVo.setCheck(cart.getIsCheck()==1);
        return cartVo;
    }

    public void updateCartNum(String cartId, String userId, String num) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(Integer.parseInt(cartId));
        cart.setUserId(Integer.parseInt(userId));
        cart.setNum(Integer.parseInt(num));
        try {
            int count = cartMapper.updateByPrimaryKeySelective(cart);
            if (count != 1) {
                throw new XmException(ExceptionEnum.UPDATE_CART_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.UPDATE_CART_ERROR);
        }
    }


    public void updateCartNumByproduct(String productId, String userId, String num) {

        Example example=new Example(ShoppingCart.class);
        example.selectProperties("id","productId","userId","num")
                .and()
                .andEqualTo("userId",userId)
                .andEqualTo("productId",productId);
        List<ShoppingCart> list= cartMapper.selectByExample(example);
        list.get(0).setNum(Integer.parseInt(num));
        try {
            int count = cartMapper.updateByPrimaryKeySelective(list.get(0));
            if (count != 1) {
                throw new XmException(ExceptionEnum.UPDATE_CART_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.UPDATE_CART_ERROR);
        }
    }

    public void deleteCart(String cartId, String userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(Integer.parseInt(cartId));
        cart.setUserId(Integer.parseInt(userId));
        try {
            cartMapper.delete(cart);
        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.DELETE_CART_ERROR);
        }
    }

    public void deleteCartByProductId(String productId, String userId) {
        Example example=new Example(ShoppingCart.class);
        example.selectProperties("id","productId","userId","num", "isCheck")
                .and()
                .andEqualTo("userId",userId)
                .andEqualTo("productId",productId);

        try {
            cartMapper.deleteByExample(example);
        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.DELETE_CART_ERROR);
        }
    }

    public void updateSelectByUserId(String userId, Integer isSelect){
        Example example=new Example(ShoppingCart.class);
        example.and()
                .andEqualTo("userId",userId);
        ShoppingCart cart =new ShoppingCart();
        cart.setIsCheck(isSelect);
        try {
            cartMapper.updateByExampleSelective(cart,example);
        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.SELECT_PRODUCT_FAIL);
        }
    }

    public void updateProductSelect(String userId, String productId, Integer isSelect){
        Example example=new Example(ShoppingCart.class);
        example.and()
                .andEqualTo("userId",userId)
                .andEqualTo("productId",productId);
        ShoppingCart cart =new ShoppingCart();
        cart.setIsCheck(isSelect);
        try {
            cartMapper.updateByExampleSelective(cart,example);
        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.SELECT_PRODUCT_FAIL);
        }
    }


}
