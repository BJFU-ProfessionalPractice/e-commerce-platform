package com.example.demo.common.controller;


import com.example.demo.common.service.CommonService;
import com.example.demo.entity.ReturnState;
import com.example.demo.entity.UserTable;
import com.example.demo.user.service.UserService;
import com.example.demo.utils.VerifyUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class UserTableController {
    @Autowired
    CommonService commonService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/")
    public String userToLogin(){
        return "/common/login";
    }

    @RequestMapping(value = "/login")
//    public String login(HttpServletRequest request, UserTable userTable) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//
//        UserTable user=commonService.userLogin(userTable);
//        if(user!=null){
//            //用户存在
//            if(user.getPassword().equals(toMD5(userTable.getPassword()))){
//                //密码正确
//                request.getSession().setAttribute("user",user);
//                if(user.getUserType()==1){
//                    //注册用户
//                    return "registeruser/index";
//                }else{
//                    //管理员用户
//                    return "admuser/index";
//                }
//            }else{
//                //用户密码错误
//                request.setAttribute("msg","用户名或密码输入错误！");
//                return "common/login";
//            }
//        }else{
//            //用户不存在
//            request.setAttribute("msg","用户名或密码输入错误！");
//            return "common/login";
//        }
//    }

    private String toMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md=MessageDigest.getInstance("MD5");
        byte[] strByteArray=str.getBytes(StandardCharsets.UTF_8);
        byte[] mdByteArray=md.digest(strByteArray);
        StringBuilder hexValue=new StringBuilder();
        for (byte b : mdByteArray) {
            int val = ((int) b) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }

    @RequestMapping(value = "/seePrivateInformation")
    public String userPrivateInformation(){
        return "registeruser/seePrivateInformation";
    }

    @RequestMapping(value = "/modifyPrivateInformation")
    public String seePrivateInformation(){
        return "registeruser/modifyPrivateInformation";
    }

    @RequestMapping("/modifiedPrivateInformationToDB")
    public void modifiedPrivateInformationToDB(HttpServletResponse response,HttpServletRequest request, UserTable userTable) throws IOException, NoSuchAlgorithmException {
        String passwordChanged=request.getParameter("passwordChanged");
        UserTable user=(UserTable) request.getSession().getAttribute("user");

        user.setUserName(userTable.getUserName());

        if(passwordChanged.equals("false")){
            //密码没有改变
            user.setPassword(userTable.getPassword());
        }else{
            //密码已经改变
            user.setPassword(toMD5(userTable.getPassword()));
        }
        user.setGender(userTable.getGender());
        user.setEmail(userTable.getEmail());
        user.setPhone(userTable.getPhone());


        int flag=commonService.updateUser(user);
        ReturnState returnState=new ReturnState();
        if(flag!=0){
            request.getSession().setAttribute("user",user);
            returnState.setState("ok");
        }else{
            returnState.setState("no");
        }
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(returnState);
        response.getWriter().write(jsonObject.toString());
    }



    @RequestMapping(value = "/registerAccount")
    public String registerAccount(){
        return "/common/registerAccount";
    }

    @RequestMapping(value = "/doRegister")
    public void doRegister(HttpServletRequest request,HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        String userType=request.getParameter("userType");
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String gender=request.getParameter("gender");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");

        UserTable userTable=new UserTable();
        userTable.setUserName(userName);
        userTable.setPassword(password);
        userTable.setGender(gender);
        userTable.setUserType(Integer.parseInt(userType));
        if(email.equals("")){
        }else{
            userTable.setEmail(email);
        }
        if(phone.equals("")){
        }else{
            userTable.setPhone(phone);
        }
        UserTable test=userService.searchUserByName(userName);
        ReturnState returnState=new ReturnState();
        if(test!=null){
            //数据库中已经存在此用户
            returnState.setState("no");
        }else{
            //数据库中不存在此用户
            userTable.setPassword(toMD5(userTable.getPassword()));
            userService.addUser(userTable);
            returnState.setState("ok");
        }
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(returnState);
        response.getWriter().write(jsonObject.toString());
    }

}
