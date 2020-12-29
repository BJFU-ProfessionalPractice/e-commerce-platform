package com.example.demo.shop.controller;

import com.example.demo.pojo.Address;
import com.example.demo.pojo.Product;
import com.example.demo.pojo.ShoppingCart;
import com.example.demo.pojo.User;
import com.example.demo.service.*;
import com.example.demo.util.StatusCode;
import com.example.demo.vo.CartVo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import com.example.demo.util.IPAddress;

@CrossOrigin
@Controller
public class ShopActionController {

    @Autowired(required = false)
    OrderService orderService;

    @Autowired(required = false)
    UserService userService;

    @Autowired(required = false)
    ShoppingCartService shoppingCartService;

    @RequestMapping("/testwsl")
    public void testwsl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(orderService.getOrder(1));

        String str = "{\"code\":0,\"msg\":\"\",\"count\":" + 1 + ",\"data\":" + "111" + "}";
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(orderService.getOrder(1).toString());
    }

    @RequestMapping("/user/login")
    public void user_login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(username);
        System.out.println(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        statusCode.setMsg("用户存在！");
        userService.login(user);
        statusCode.setUsername(username);

        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }
    @RequestMapping("/carts/products/sum")
    public void carts_products_sum(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StatusCode statusCode=new StatusCode();

        User user= userService.getUserByName(request.getParameter("username"));

//        System.out.println(user.toString());
        List<CartVo> list= shoppingCartService.getCartByUserId(""+user.getUserId());

        statusCode.setCount(list.size());
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/carts")
    public void carts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username= request.getParameter("username");
        User user= userService.getUserByName(request.getParameter("username"));

        StatusCode statusCode=new StatusCode();
        initStateCode(statusCode, ""+user.getUserId());
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/carts/{username}/{productId}")
    public void carts(@PathVariable String username, @PathVariable String productId ,HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user= userService.getUserByName(username);
        shoppingCartService.addCart(productId, ""+user.getUserId());
        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/carts/{username}/{productId}/{num}")
    public void carts(@PathVariable String username, @PathVariable String productId , @PathVariable String num,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= userService.getUserByName(username);
        shoppingCartService.updateCartNumByproduct(productId,""+user.getUserId(),num);
        StatusCode statusCode=new StatusCode();
        initStateCode(statusCode, ""+user.getUserId());
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }


    @RequestMapping("/carts/remove/{username}/{productId}")
    public void carts_remove(@PathVariable String username, @PathVariable String productId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= userService.getUserByName(username);
        shoppingCartService.deleteCartByProductId(productId, ""+user.getUserId());


        StatusCode statusCode=new StatusCode();
        initStateCode(statusCode, ""+user.getUserId());
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    public void initStateCode(StatusCode statusCode, String userId) throws UnknownHostException {

        List<CartVo> list = shoppingCartService.getCartByUserId(userId);
        int cartTotalPrice=0,cartTotalQuantity=0;
        int selectCount=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).isCheck()){
                selectCount+=list.get(i).getNum();
            }
            cartTotalPrice+=list.get(i).getPrice()*list.get(i).getNum();
            cartTotalQuantity+=list.get(i).getNum();
            list.get(i).setProductImg(IPAddress.getIP()+"/public/imgs/productList/phone.png");
        }
        statusCode.setCartTotalPrice(cartTotalPrice);
        statusCode.setCartTotalQuantity(cartTotalQuantity);
        statusCode.setCode(1);
        statusCode.setSelectCount(selectCount);
        statusCode.setData(list);
    }

    @RequestMapping("/carts/selectAll/{username}")
    public void carts_selectAll(@PathVariable String username ,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= userService.getUserByName(username);
        shoppingCartService.updateSelectByUserId(""+user.getUserId(), 1);

        StatusCode statusCode=new StatusCode();
        initStateCode(statusCode, ""+user.getUserId());
        statusCode.setSelectAll(true);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/carts/select/{username}/{productId}/{isSelect}")
    public void carts_selectAll(@PathVariable String username, @PathVariable String productId, @PathVariable Integer isSelect
            ,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= userService.getUserByName(username);
        shoppingCartService.updateProductSelect(""+user.getUserId(), productId, isSelect);

        StatusCode statusCode=new StatusCode();
        initStateCode(statusCode, ""+user.getUserId());
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/carts/unSelectAll/{username}")
    public void unSelectAll(@PathVariable String username ,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= userService.getUserByName(username);
        shoppingCartService.updateSelectByUserId(""+user.getUserId(), 0);


        StatusCode statusCode=new StatusCode();
        initStateCode(statusCode, ""+user.getUserId());
        statusCode.setSelectAll(false);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }


    @Autowired(required = false)
    ProductService productService;

    @RequestMapping("/products")
    public void products(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int categroy = Integer.parseInt(request.getParameter("categoryId"));
        int pagesize = Integer.parseInt(request.getParameter("pageSize"));
        List<Product> list=productService.getProductByCategoryId(categroy);

        list = list.subList(0,Math.min(pagesize,list.size()));
        for(int i=0;i<list.size();i++){
            list.get(i).setProductPicture(IPAddress.getIP()+"/public/imgs/phone/new.png");
        }

        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        statusCode.setData(list);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }


    @RequestMapping("/products/{productID}")
    public void products(@PathVariable String productID ,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product =productService.getProductById(productID);
        product.setProductPicture(IPAddress.getIP()+"/public/imgs/phone/new.png");

        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        statusCode.setData(product);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }



    @RequestMapping("/user/register")
    public void user_register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());

        userService.register(user);
        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        statusCode.setUsername(user.getUsername());
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }


    @RequestMapping("/user/logout")
    public void user_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }


    @Autowired(required = false)
    AddressService addressService;

    @RequestMapping("/shippings/{username}")
    public void shippings(@PathVariable String username ,HttpServletResponse response, HttpServletRequest request) throws IOException {

        User user= userService.getUserByName(username);
        List<Address> list=addressService.getAllAddress(""+user.getUserId());
        for(int i=0;i<list.size();i++){
            if(list.get(i).getIsDelete()==1){
                list.remove(i);
                i--;
            }
        }
        StatusCode statusCode=new StatusCode();
        statusCode.setCode(1);
        statusCode.setData(list);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(statusCode);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/address/delete/{addressId}")
    public void address_delete(@PathVariable String addressId ,HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println(addressId);
        addressService.deleteAddressById(addressId);

    }

    @RequestMapping("/address/add")
    public void address_delete(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String username = request.getParameter("username");
        String people =request.getParameter("people");
        System.out.println(people);
        String location= request.getParameter("location");
        String tag= request.getParameter("tag");
        String phone =request.getParameter("phone");

        User user= userService.getUserByName(username);
        Address address=new Address();
        address.setPeople(people);
        address.setIsDelete(0);
        address.setLocation(location);
        address.setPhone(phone);
        address.setTag(tag);
        address.setShopuserId(user.getUserId());
        addressService.addAddress(address);
    }






}
