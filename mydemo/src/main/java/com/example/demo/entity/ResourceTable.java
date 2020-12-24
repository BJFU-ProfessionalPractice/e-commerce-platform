package com.example.demo.entity;

import javax.swing.*;
import java.sql.Struct;
import java.sql.Timestamp;


public class ResourceTable implements Comparable<ResourceTable> {
    private int resourceID;
    private String resourceName;
    private int resourceType;
    private String resourceLocation;
    private int visitVolume;
    private Timestamp uploadTime;
    private String introduction;
    private int userID;

    @Override
    public String toString() {
        String type=null;
        if(resourceType==1){
            type="论文";
        }else if(resourceType==2){
            type="代码";
        }else{
            type="数据集";
        }
        return "{\"resourceID\":"+resourceID+",\"resourceName\":"+"\""+resourceName+"\""
                +",\"resourceType\":"+"\""+type+"\""
                +",\"visitVolume\":"+"\""+visitVolume+"\""
                +",\"uploadTime\":"+"\""+this.getUploadTimeString()+"\"}";
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public int getVisitVolume() {
        return visitVolume;
    }

    public void setVisitVolume(int visitVolume) {
        this.visitVolume = visitVolume;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public String getUploadTimeString() {
        return uploadTime.toString().substring(0,uploadTime.toString().lastIndexOf('.'));
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime =  uploadTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getIntroductionString() {
        if(introduction!=null){
            return introduction;
        }else{
            return "";
        }
    }


    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public int compareTo(ResourceTable o) {
        return this.getResourceID()-o.getResourceID();
    }
}
