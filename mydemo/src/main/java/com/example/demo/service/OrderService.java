package com.example.demo.service;

import com.example.demo.exception.ExceptionEnum;
import com.example.demo.exception.XmException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.pojo.Address;
import com.example.demo.pojo.Order;
import com.example.demo.pojo.Product;
import com.example.demo.pojo.ShoppingCart;
import com.example.demo.util.IdWorker;
import com.example.demo.vo.CartVo;
import com.example.demo.vo.OrderVo;
import com.example.demo.vo.OrderVoVo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class OrderService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AddressMapper addressMapper;


    private final static String SECKILL_PRODUCT_USER_LIST = "seckill:product:user:list";

    @Transactional
    public String addOrder(List<CartVo> cartVoList, Integer userId, Integer addressId) {
        // 先添加订单
        String orderId = idWorker.nextId() + ""; // 订单id

        long time = new Date().getTime(); // 订单生成时间
        for (CartVo cartVo : cartVoList) {
            Order order = new Order();
            order.setOrderId(orderId);
            order.setOrderTime(time);
            order.setProductNum(cartVo.getNum());
            order.setProductId(cartVo.getProductId());
            order.setProductPrice(cartVo.getPrice());
            order.setUserId(userId);
            order.setState(0);
            order.setAddressId(addressId);

            try {
                System.out.println(order.toString());
                orderMapper.insert(order);
            } catch (Exception e) {
                e.printStackTrace();
                throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
            }
            // 减去商品库存,记录卖出商品数量
            // TODO : 此处会产生多线程问题，即不同用户同时对这个商品操作，此时会导致数量不一致问题
            Product product = productMapper.selectByPrimaryKey(cartVo.getProductId());
            product.setProductNum(product.getProductNum() - cartVo.getNum());
            product.setProductSales(product.getProductSales() + cartVo.getNum());
            productMapper.updateByPrimaryKey(product);
        }
        // 删除购物车
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        try {
            int count = cartMapper.delete(cart);
            if (count == 0) {
                throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
        }
        return orderId;
    }

    public List<List<OrderVo>> getOrder(Integer userId) {
        List<OrderVo> list = null;
        ArrayList<List<OrderVo>> ret = new ArrayList<>();
        try {
            list = orderMapper.getOrderVoByUserId(userId);
            if (ArrayUtils.isEmpty(list.toArray())) {
                throw new XmException(ExceptionEnum.GET_ORDER_NOT_FOUND);
            }
            // 将同一个订单放在一组
            Map<String, List<OrderVo>> collect = list.stream().collect(Collectors.groupingBy(Order::getOrderId));
            Collection<List<OrderVo>> values = collect.values();
            ret.addAll(values);
        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_ORDER_ERROR);
        }
        return ret;
    }


    public List<OrderVoVo> getOrderVoVo(Integer userId){
        List<OrderVoVo> orderVoVoList = new ArrayList<>();
        try {
            Example example=new Example(Order.class);
            example.selectProperties("orderId","productNum","addressId","productPrice", "state","orderTime").and()
                    .andEqualTo("userId",userId);
            List<Order> orderList = orderMapper.selectByExample(example);
            Set<String> orderIdSet=new HashSet<>();
            for(int i=0;i<orderList.size();i++){
                orderIdSet.add(""+orderList.get(i).getOrderId());
                System.out.println(orderList.get(i).getAddressId());
            }
            orderIdSet.forEach(v->{
                        OrderVoVo orderVoVo=new OrderVoVo();
                        Double total= 0.0;
                        List<OrderVo> orderList1=new ArrayList<>();
                        for(int i=0;i<orderList.size();i++){
                            if(orderList.get(i).getOrderId().equals(v)){
                                total+=orderList.get(i).getProductPrice()*orderList.get(i).getProductNum();
                                final Date date = new Date(orderList.get(i).getOrderTime());//新建一个时间对象
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                final String yourtime = sdf.format(date);
                                orderVoVo.setTime(yourtime);
                                orderVoVo.setState(orderList.get(i).getState());
                                Address address =addressMapper.selectByPrimaryKey(orderList.get(i).getAddressId());
                                orderVoVo.setAddress(address);
                                OrderVo orderVo = new OrderVo();
                                orderVo.setProductNum(orderList.get(i).getProductNum());
                                orderVo.setProductPrice(orderList.get(i).getProductPrice());
                                Example example1 = new Example(Product.class);
                                example1.selectProperties("productName","productPicture")
                                        .and()
                                        .andEqualTo("productId",orderList.get(i).getProductId());
                                Product product = productMapper.selectByExample(example).get(0);
                                orderVo.setProductName(product.getProductName());
                                orderVo.setProductPicture(product.getProductPicture());
                                orderList1.add(orderVo);
                            }
                        }
                        orderVoVo.setOrderList(orderList1);
                        orderVoVo.setTotalPrice(total);
                        orderVoVoList.add(orderVoVo);
            }
            );

        } catch (XmException e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_ORDER_ERROR);
        }
        return orderVoVoList;
    }


}
