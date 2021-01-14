package com.zero.system.model;

import com.zero.system.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Bag_file {
    private Integer bid;
    private String bname;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date creattime;
    private String creattimeStr;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endtime;
    private String endtimeStr;
    private int aid;
    //添加元素

    private String adminName;
    private Admin admin;
    private List<Files> filesList;

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Date getEndtime() {

        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<Files> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<Files> filesList) {
        this.filesList = filesList;
    }

    public String getCreattimeStr() {
        if(creattime!=null){
            creattimeStr= DateUtil.date2String(creattime,"yyyy-MM-dd");
        }
        return creattimeStr;
    }

    public void setCreattimeStr(String creattimeStr) {
        this.creattimeStr = creattimeStr;
    }

    public String getEndtimeStr() {
        if(endtime!=null){
            endtimeStr= DateUtil.date2String(endtime,"yyyy-MM-dd");
        }
        return endtimeStr;
    }

    public void setEndtimeStr(String endtimeStr) {
        this.endtimeStr = endtimeStr;
    }
}
