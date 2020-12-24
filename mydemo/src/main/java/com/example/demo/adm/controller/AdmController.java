package com.example.demo.adm.controller;

import com.example.demo.adm.service.AdmService;
import com.example.demo.entity.DownLoad;
import com.example.demo.entity.ResourceTable;
import com.example.demo.entity.ReturnState;
import com.example.demo.entity.UserTable;
import com.example.demo.user.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class AdmController {
    @Autowired
    AdmService admService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/seePrivateInformationAdm")
    public String seePrivateInformationAdm(){
        return "/admuser/seePrivateInformationAdm";
    }

    @RequestMapping(value = "/modifyPrivateInformationAdm")
    public String modifyPrivateInformationAdm(){
        return "/admuser/modifyPrivateInformationAdm";
    }

    @RequestMapping(value = "/registerUserManage")
    public String registerUserManage(){
        return "/admuser/registerUserManage";
    }

    @RequestMapping(value = "/reloadRegisterUser")
    public void reloadRegisterUser(@RequestParam String page, @RequestParam String limit, HttpServletRequest request
                                    , HttpServletResponse response) throws IOException {
        List<UserTable> userList=admService.searchRegisterUser();
        request.setAttribute("registerUserList",userList);
        String userName= request.getParameter("userName");
        if(!userName.equals("")){
            for(int i = 0; i<userList.size(); i++){
                if(!userList.get(i).getUserName().contains(userName)){
                    userList.remove(i);
                    i--;
                }
            }
        }
        int count=0;
        if(userList!=null&&userList.size()!=0){
            count=userList.size();
            if(((Integer.parseInt(page))*Integer.parseInt(limit))<=count){
                userList=userList.subList((Integer.parseInt(page)-1)*Integer.parseInt(limit),((Integer.parseInt(page))*Integer.parseInt(limit)));
            }else{
                userList=userList.subList((Integer.parseInt(page)-1)*Integer.parseInt(limit),count);
            }
        }

        String jsonString="";
        assert userList != null;
        for(UserTable userTable:userList){
            jsonString=jsonString+","+userTable.toString();
        }
        if(jsonString.equals("")){
            jsonString="[]";
        }else{
            jsonString="["+jsonString.substring(1)+"]";
        }

        String str = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + jsonString + "}";
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(str);
    }

    @RequestMapping(value = "/usersee/{userID}")
    public String userSee(@PathVariable String userID,HttpServletRequest request){
        int userid=Integer.parseInt(userID);
        UserTable userTable=userService.searchUserByID(userid);
        request.setAttribute("userOfToSee",userTable);
        return "/admuser/registerUserInfo";
    }

    @RequestMapping(value = "/userdelete/{userID}")
    public void userDelete(@PathVariable String userID,HttpServletRequest request,HttpServletResponse response) throws IOException {
        int userid=Integer.parseInt(userID);
        UserTable userTable=userService.searchUserByID(userid);
        List<ResourceTable> resourceTableList=userService.searchResourceByUserID(userid);
        //删除该用户所用的资源
        ReturnState returnState=new ReturnState();
        for(ResourceTable re:resourceTableList){
            userService.deleteRelationByResourceID(re.getResourceID());
            userService.deleteBelongByResourceID(re.getResourceID());
            userService.deleteDiscussByResourceID(re.getResourceID());
            userService.deleteDownLoadByResourceID(re.getResourceID());
            userService.deleteResourceByID(re.getResourceID());
            String home=System.getProperty("user.dir");
            String deleteFilePath ="\\src\\main\\resources\\";
            File file=new File(home+deleteFilePath+re.getResourceLocation());
            boolean flag=file.delete();
            if(flag){
                returnState.setState("ok");
            }else{
                returnState.setState("no");
            }
        }
        //删除改用户所用的download记录
        admService.deleteDownLoadByUserID(userid);
        //删除该用户所有的discuss记录
        admService.deleteDiscussByUserID(userid);
        //删除该用户
        admService.deleteUserByName(userTable.getUserName());

        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(returnState);
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping(value = "/ResourceManage")
    public String resourceManage(){
        return "/admuser/resourceStatistic";
    }

    @RequestMapping(value = "/reloadResourceTableAdm")
    public void reloadResourceTableAdm(@RequestParam String page,@RequestParam String limit,@RequestParam String resourceType,@RequestParam String territoryID,@RequestParam String resourceName,HttpServletRequest request,HttpServletResponse response) throws IOException {

        List<ResourceTable> resourceTableList=null;
        List<ResourceTable> resourceTableList1=userService.searchResourceByType(1);
        List<ResourceTable> resourceTableList2=userService.searchResourceByType(2);
        List<ResourceTable> resourceTableList3=userService.searchResourceByType(3);
        if(resourceType.equals("")){
            resourceTableList1.addAll(resourceTableList2);
            resourceTableList1.addAll(resourceTableList3);
            resourceTableList=resourceTableList1;
        }else{
            if(resourceType.equals("1")){
                resourceTableList=resourceTableList1;
            }else if(resourceType.equals("2")){
                resourceTableList=resourceTableList2;
            }else if(resourceType.equals("3")){
                resourceTableList=resourceTableList3;
            }
        }
        if(!territoryID.equals("")){
            int territoryid=Integer.parseInt(territoryID);
            assert resourceTableList != null;
            for(int i = 0; i<resourceTableList.size(); i++){
                if(userService.searchBelongByResourceIDAndTerritoryID(resourceTableList.get(i).getResourceID(),territoryid)==null){
                    resourceTableList.remove(i);
                    i--;
                }
            }
        }
        if(!resourceName.equals("")){
            assert resourceTableList != null;
            for(int i = 0; i<resourceTableList.size(); i++){
                if(!resourceTableList.get(i).getResourceName().contains(resourceName)){
                    resourceTableList.remove(i);
                    i--;
                }
            }
        }
        assert resourceTableList != null;
        for(ResourceTable re:resourceTableList){
            List<DownLoad> downLoadList=userService.searchDownLoadByResourceID(re.getResourceID());
            re.setVisitVolume(downLoadList.size());
        }
        int count=0;
        if(resourceTableList.size() != 0){
            count=resourceTableList.size();
            if(((Integer.parseInt(page))*Integer.parseInt(limit))<=count){
                resourceTableList=resourceTableList.subList((Integer.parseInt(page)-1)*Integer.parseInt(limit),((Integer.parseInt(page))*Integer.parseInt(limit)));
            }else{
                resourceTableList=resourceTableList.subList((Integer.parseInt(page)-1)*Integer.parseInt(limit),count);
            }
        }
        String jsonString="";
        for(ResourceTable resourceTable:resourceTableList){
            jsonString=jsonString+","+resourceTable.toString();
        }
        if(jsonString.equals("")){
            jsonString="[]";
        }else{
            jsonString="["+jsonString.substring(1)+"]";
        }

        String str = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + jsonString + "}";
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(str);
    }

}
