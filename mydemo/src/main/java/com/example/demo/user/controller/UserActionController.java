package com.example.demo.user.controller;


import com.example.demo.entity.*;
import com.example.demo.user.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.plugin2.main.server.JVMHealthData;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class UserActionController {

    @Autowired(required = false)
    UserService userService;


    @RequestMapping("/uploadResource")
    public String uploadPaperResource(HttpServletRequest request){
        int type=Integer.parseInt(request.getParameter("type"));
        List<ResourceTable> list1=userService.searchResourceByType(1);
        List<ResourceTable> list2=userService.searchResourceByType(2);
        List<ResourceTable> list3=userService.searchResourceByType(3);
        if(type==1){
            list2.addAll(list3);
            sortResource(list2);
            request.setAttribute("resources",list2);
            return "registeruser/uploadPaperResource";
        }else if(type==2){
            sortResource(list1);
            request.setAttribute("resources",list1);
            return "registeruser/uploadCodeResource";
        }else{
            sortResource(list1);
            request.setAttribute("resources",list1);
            return "registeruser/uploadDataSetResource";
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String testUploadFile(HttpServletRequest request, MultipartHttpServletRequest multiReq) {

        int type=Integer.parseInt(request.getParameter("type"));
        String introduction=request.getParameter("introduction");
        String[] territory=request.getParameterValues("territory");
        String[] relation=request.getParameterValues("relation");
        String uploadPath =System.getProperty("user.dir")+"\\src\\main\\resources\\uploadresource\\";
        // 获取上传文件的路径
        String uploadFilePath = multiReq.getFile("myfile").getOriginalFilename();
        if(uploadFilePath.trim().length()==0){
            return "/common/nofileerror";
        }
        // 截取上传文件的文件名
        String uploadFileName = uploadFilePath.substring(
                uploadFilePath.lastIndexOf('\\') + 1);
        if(userService.searchResourceByName(uploadFileName)!=null){
            return "/common/samefileerror";
        }

        OutputStream fos = null;
        InputStream fis = null;
        String resourcelocation=uploadPath+ uploadFileName;
        try {
            fis = (InputStream) multiReq.getFile("myfile").getInputStream();
            fos = new FileOutputStream(new File( resourcelocation));
            byte[] temp = new byte[1024];
            int i = fis.read(temp);
            while (i != -1){
                fos.write(temp,0,temp.length);
                fos.flush();
                i = fis.read(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        UserTable user= (UserTable) request.getSession().getAttribute("user");
        ResourceTable rt=new ResourceTable();
        if(!introduction.equals("")){
            rt.setIntroduction(introduction);
        }
        rt.setUserID(user.getUserID());
        rt.setResourceLocation("uploadresource\\"+uploadFileName);
        rt.setResourceType(type);
        Timestamp time=new Timestamp(System.currentTimeMillis());
        rt.setUploadTime(time);
        rt.setResourceName(uploadFileName);
        int flag=userService.addResource(rt);

        if(territory!=null){
            for(String s:territory){
                Belong belong=new Belong();
                belong.setTerritoryID(Integer.parseInt(s));
                belong.setResourceID(rt.getResourceID());
                userService.addBelong(belong);
            }
        }
        if(relation!=null){
            for (String r:relation){
                Relation re=new Relation();
                re.setResourceIDOne(rt.getResourceID());
                re.setResourceIDTwo(Integer.parseInt(r));
                userService.addRelation(re);
            }
        }
        if(flag!=0){
            return "/common/successupload";
        }else{
            return "/common/unsuccessupload";
        }
    }

    private void sortResource(List<ResourceTable> resourceList){
        Collections.sort(resourceList);
    }

    @RequestMapping(value = "/findResource")
    public String finResource(HttpServletRequest request){
        return "/registeruser/findResource";
    }


    @RequestMapping(value = "/reloadResource")
    public void reloadResource(@RequestParam String page,@RequestParam String limit,@RequestParam String resourceType,@RequestParam String territoryID,@RequestParam String resourceName,HttpServletRequest request,HttpServletResponse response) throws IOException {
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
        int count=0;
        if(resourceTableList!=null&&resourceTableList.size()!=0){
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

    @RequestMapping(value = "/territorySearch/{territoryID}")
    public void territorySearch(@PathVariable String territoryID,HttpServletResponse response) throws IOException {
        if(!territoryID.equals("")){
            TerritoryTable territoryTable=userService.searchTerritoryByID(Integer.parseInt(territoryID));
            response.setContentType("application/json;charset=utf-8");
            JSONObject jsonObject=JSONObject.fromObject(territoryTable);
            response.getWriter().write(jsonObject.toString());
        }
    }


    @RequestMapping(value = "/resourcesee/{resourceID}")
    public String resourceSee(@PathVariable String resourceID,HttpServletRequest request){
        HttpSession session=request.getSession();
        int resource_id=Integer.parseInt(resourceID);
        UserTable user= (UserTable) session.getAttribute("user");
        ResourceTable re=userService.searchResourceByID(resource_id);

        List<Belong> belongList=userService.searchBelongByResourceID(resource_id);

        String territoryList="";
        for(int i=0;i<belongList.size();i++){
            TerritoryTable te=userService.searchTerritoryByID(belongList.get(i).getTerritoryID());
            territoryList =territoryList+" | "+te.getTerritoryName();
        }
        UserTable uploadUser=userService.searchUserByID(re.getUserID());
        request.setAttribute("resource",re);
        request.setAttribute("territoryList",territoryList);
        request.setAttribute("uploadUser",uploadUser);
        return "/registeruser/seeResource";
    }

    @RequestMapping(value = "/reloadRelationResourceTable")
    public void reloadRelationResourceTable(HttpServletResponse response,HttpServletRequest request) throws IOException {

        String resourceID=request.getParameter("resourceID");
        int resource_id=Integer.parseInt(resourceID);
        ResourceTable re=userService.searchResourceByID(resource_id);
        List<Relation> relationList=null;
        if(re.getResourceType()==1){
            relationList=userService.searchRelationByOne(resource_id);
        }else{
            relationList=userService.searchRelationByTwo(resource_id);
        }
        List<ResourceTable> resourceTableList=new ArrayList<ResourceTable>();
        for(int i=0;i<relationList.size();i++){
            if(re.getResourceType()==1){
                resourceTableList.add(userService.searchResourceByID(relationList.get(i).getResourceIDTwo()));
            }else{
                resourceTableList.add(userService.searchResourceByID(relationList.get(i).getResourceIDOne()));
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

        String str = "{\"code\":0,\"msg\":\"\",\"count\":" + resourceTableList.size() + ",\"data\":" + jsonString + "}";
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(str);
    }


    @RequestMapping(value = "/reloadDiscussList")
    public void reloadDiscussList(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String resourceID=request.getParameter("resourceID");
        List<Discuss> discussList=userService.searchDiscussByResourceID(Integer.parseInt(resourceID));
        List<MyDiscuss> myDiscussList= new ArrayList<>();
        for(int i=0;i<discussList.size();i++){
            UserTable userTable=userService.searchUserByID(discussList.get(i).getUserID());
            MyDiscuss myDiscuss=new MyDiscuss();
            myDiscuss.setDiscussID(discussList.get(i).getDiscussID());
            myDiscuss.setDiscussContent(discussList.get(i).getDiscussContent());
            myDiscuss.setResourceID(discussList.get(i).getResourceID());
            myDiscuss.setUserName(userTable.getUserName());
            myDiscussList.add(myDiscuss);
        }
        String jsonString="";
        for(MyDiscuss myDiscuss:myDiscussList){
            jsonString=jsonString+","+myDiscuss.toString();
        }
        if(jsonString.equals("")){
            jsonString="[]";
        }else{
            jsonString="["+jsonString.substring(1)+"]";
        }

        String str = "{\"code\":0,\"msg\":\"\",\"count\":" + myDiscussList.size() + ",\"data\":" + jsonString + "}";
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(str);
    }

    @RequestMapping(value = "/uploadDiscuss")
    public void uploadDiscuss(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String resourceID=request.getParameter("resourceID");
        String mydiscuss=request.getParameter("mydiscuss");
        int uploadDiscussUserID=((UserTable)request.getSession().getAttribute("user")).getUserID();
        Discuss discuss=new Discuss();
        discuss.setDiscussContent(mydiscuss);
        discuss.setResourceID(Integer.parseInt(resourceID));
        discuss.setUserID(uploadDiscussUserID);
        userService.addDiscuss(discuss);
    }

    @RequestMapping(value = "/deleteDiscuss")
    public void deleteDiscuss(HttpServletRequest request,HttpServletResponse response) throws IOException {
        int discussID=Integer.parseInt(request.getParameter("discussID"));
        String userName=request.getParameter("userName");
        UserTable userTable=(UserTable)request.getSession().getAttribute("user");
        ReturnState returnState=new ReturnState();
        if(userTable.getUserName().equals(userName)){
            //可以执行删除操作
            userService.deleteDiscussByID(discussID);
            returnState.setState("ok");
        }else{
            //不能执行删除操作
            returnState.setState("no");
        }
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(returnState);
        response.getWriter().write(jsonObject.toString());
    }

    @ResponseBody
    @RequestMapping(value = "/downloadResource/{resourceID}")
    public void downloadResource(@PathVariable String resourceID, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        ResourceTable resourceTable=userService.searchResourceByID(Integer.parseInt(resourceID));

        UserTable userTable= (UserTable) request.getSession().getAttribute("user");
        DownLoad downLoad=new DownLoad();
        downLoad.setUserID(userTable.getUserID());
        downLoad.setResourceID(resourceTable.getResourceID());
        Timestamp time=new Timestamp(System.currentTimeMillis());
        downLoad.setDownLoadTime(time);
        userService.addDownLoad(downLoad);

        String location=resourceTable.getResourceLocation();

        String[] strs=location.split("\\\\");
        String fileName = strs[strs.length-1];

        String home=System.getProperty("user.dir");
        String downloadFilePath ="\\src\\main\\resources\\";

        File file = new File(home+downloadFilePath+location);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开  
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                response.addHeader("content-Disposition", "attachment;fileName==?UTF-8?B?"+new String(Base64.getEncoder().encode(fileName.getBytes(StandardCharsets.UTF_8)))+"?=");
            }else if(request.getHeader("User-Agent").toUpperCase().contains("EDGE")){
                response.addHeader("content-Disposition", "attachment;fileName="+ URLEncoder.encode(fileName, "utf-8"));
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");// 谷歌
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            }else{
                fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception ignored) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @RequestMapping(value ="/seeUploaded")
    public String seeUserUploaded(HttpServletRequest request){
        return "/registeruser/seePrivateUploaded";
    }

    @RequestMapping(value = "/reloadUploadedResourceTable")
    public void reloadUploadedResourceTable(@RequestParam String page,@RequestParam String limit,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String resourceName=request.getParameter("resourceName");
        String resourceType=request.getParameter("resourceType");
        String territoryID=request.getParameter("territoryID");
        UserTable userTable= (UserTable) request.getSession().getAttribute("user");

        List<ResourceTable> resourceTableList=userService.searchResourceByUserID(userTable.getUserID());
        if(!resourceType.equals("")){
            for(int i=0;i<resourceTableList.size();i++){
                if(resourceTableList.get(i).getResourceType()!=Integer.parseInt(resourceType)){
                    resourceTableList.remove(i);
                    i--;
                }
            }
        }
        if(!territoryID.equals("")){
            int territoryid=Integer.parseInt(territoryID);
            for(int i = 0; i<resourceTableList.size(); i++){
                if(userService.searchBelongByResourceIDAndTerritoryID(resourceTableList.get(i).getResourceID(),territoryid)==null){
                    resourceTableList.remove(i);
                    i--;
                }
            }
        }
        if(!resourceName.equals("")){
            for(int i = 0; i<resourceTableList.size(); i++){
                if(!resourceTableList.get(i).getResourceName().contains(resourceName)){
                    resourceTableList.remove(i);
                    i--;
                }
            }
        }

        int count=0;
        if(resourceTableList!=null&&resourceTableList.size()!=0){
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

    @RequestMapping(value = "/deleteResource")
    public void deleteResource(HttpServletRequest request,HttpServletResponse response) throws IOException {

        int resourceid=Integer.parseInt(request.getParameter("resourceID"));

        ResourceTable resourceTable=userService.searchResourceByID(resourceid);
        userService.deleteRelationByResourceID(resourceid);
        userService.deleteBelongByResourceID(resourceid);
        userService.deleteDiscussByResourceID(resourceid);
        userService.deleteDownLoadByResourceID(resourceid);
        userService.deleteResourceByID(resourceid);
        //删除物理文件
        String home=System.getProperty("user.dir");
        String deleteFilePath ="\\src\\main\\resources\\";
        File file=new File(home+deleteFilePath+resourceTable.getResourceLocation());
        boolean flag=file.delete();
        ReturnState returnState=new ReturnState();
        if(flag){
            returnState.setState("ok");
        }else{
            returnState.setState("no");
        }
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject=JSONObject.fromObject(returnState);
        response.getWriter().write(jsonObject.toString());
    }



    @RequestMapping(value = "/modifyResource/{resourceID}")
    public String modifyResource(@PathVariable String resourceID,HttpServletRequest request){
        int resourceid=Integer.parseInt(resourceID);
        ResourceTable resourceTable=userService.searchResourceByID(resourceid);
        List<ResourceTable> list1=userService.searchResourceByType(2); //代码资源
        List<ResourceTable> list2=userService.searchResourceByType(3); //数据集资源
        List<ResourceTable> list3=userService.searchResourceByType(1); //论文资源

        request.setAttribute("thisResource",resourceTable);
        TerritoryFlag territoryFlag=new TerritoryFlag();

        List<Belong> belongList=userService.searchBelongByResourceID(resourceid);
        territoryFlag.setOneFlag("false");
        territoryFlag.setTwoFlag("false");
        territoryFlag.setThreeFlag("false");
        territoryFlag.setFourFlag("false");
        territoryFlag.setFiveFlag("false");
        territoryFlag.setSixFlag("false");
        territoryFlag.setSevenFlag("false");
        territoryFlag.setEightFlag("false");
        for(Belong be:belongList){
            if(be.getTerritoryID()==1){
                territoryFlag.setOneFlag("true");
            }else if(be.getTerritoryID()==2){
                territoryFlag.setTwoFlag("true");
            }else if(be.getTerritoryID()==3){
                territoryFlag.setThreeFlag("true");
            }else if(be.getTerritoryID()==4){
                territoryFlag.setFourFlag("true");
            }else if(be.getTerritoryID()==5){
                territoryFlag.setFiveFlag("true");
            }else if(be.getTerritoryID()==6){
                territoryFlag.setSixFlag("true");
            }else if(be.getTerritoryID()==7){
                territoryFlag.setSevenFlag("true");
            }else if(be.getTerritoryID()==8){
                territoryFlag.setEightFlag("true");
            }
        }
        request.setAttribute("territoryFlag",territoryFlag);
        List<ResourceTable> resources=null;
        if(resourceTable.getResourceType()==1){
            list1.addAll(list2);
            sortResource(list1);
            resources=list1;
        }else if(resourceTable.getResourceType()==2){
            sortResource(list3);
            resources=list3;
        }else if(resourceTable.getResourceType()==3){
            sortResource(list3);
            resources=list3;
        }
        List<ResourceFull> resourceFullList=new ArrayList<ResourceFull>();
        assert resources != null;
        for(ResourceTable re:resources){
            Relation relation=new Relation();
            if(resourceTable.getResourceType()==1){
                relation.setResourceIDOne(resourceTable.getResourceID());
                relation.setResourceIDTwo(re.getResourceID());
            }else{
                relation.setResourceIDTwo(resourceTable.getResourceID());
                relation.setResourceIDOne(re.getResourceID());
            }
            ResourceFull resourceFull=new ResourceFull();
            resourceFull.setResourceID(re.getResourceID());
            resourceFull.setIntroduction(re.getIntroduction());
            resourceFull.setResourceLocation(re.getResourceLocation());
            resourceFull.setResourceName(re.getResourceName());
            resourceFull.setResourceType(re.getResourceType());
            resourceFull.setUploadTime(re.getUploadTime());
            resourceFull.setVisitVolume(re.getVisitVolume());
            resourceFull.setUserID(re.getUserID());
            if(userService.searchRelationByOneAndTwo(relation)==null){
                resourceFull.setRelationFlag("false");
            }else{
                resourceFull.setRelationFlag("true");
            }
            resourceFullList.add(resourceFull);
        }
        request.setAttribute("resources",resourceFullList);
        return "/registeruser/modifyPrivateResourceInfo";
    }


    @RequestMapping(value = "/doResourceInfoModify")
    public String doResourceInfoModify(HttpServletRequest request){
        String resourceName=request.getParameter("resourceName");
        String[] territory=request.getParameterValues("territory");
        String[] relation=request.getParameterValues("relation");
        String introduction=request.getParameter("introduction");
        ResourceTable resourceTable= userService.searchResourceByName(resourceName);

        UserTable user= (UserTable) request.getSession().getAttribute("user");
        resourceTable.setIntroduction(introduction);
        int flag=userService.updateResource(resourceTable);

        //删除数据库中存在的belong
        userService.deleteBelongByResourceID(resourceTable.getResourceID());
        if(territory!=null){
            for(String s:territory){
                Belong belong=new Belong();
                belong.setTerritoryID(Integer.parseInt(s));
                belong.setResourceID(resourceTable.getResourceID());
                userService.addBelong(belong);
            }
        }

        //删除数据库中存在的relation
        userService.deleteRelationByResourceID(resourceTable.getResourceID());
        if(relation!=null){
            for (String r:relation){
                Relation re=new Relation();
                if(resourceTable.getResourceType()==1){
                    re.setResourceIDOne(resourceTable.getResourceID());
                    re.setResourceIDTwo(Integer.parseInt(r));
                }else{
                    re.setResourceIDTwo(resourceTable.getResourceID());
                    re.setResourceIDOne(Integer.parseInt(r));
                }
                userService.addRelation(re);
            }
        }
        return "redirect:/resourcesee/"+resourceTable.getResourceID();
    }

}
