package com.zero.system.controller;

import com.zero.system.model.Admin;
import com.zero.system.model.Bag_file;
import com.zero.system.service.BagFileService;
import com.zero.system.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class BagFileController {
    @Autowired
    private BagFileService bagFileService;

    @Autowired
    private AjaxResult ajaxResult;


    @GetMapping("/bagFile")
    public String admin(){
        return "manager/file_manager/FileBagList";
    }

    @RequestMapping("/bagfilein")
    public String bagfilein(Integer bid, Model modeld){
        modeld.addAttribute("bid",bid);
        return "/manager/file_manager/FileList";
    }

    @RequestMapping("/FileBagList")
    @ResponseBody
    public Object adminList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            String bname,String creattime,String endtime,String adminName){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(adminName)) paramMap.put("adminName",adminName);
        if(!StringUtils.isEmpty(bname)) paramMap.put("bname",bname);
        if(!StringUtils.isEmpty(creattime)) paramMap.put("creattime",creattime);
        if(!StringUtils.isEmpty(endtime)) paramMap.put("endtime",endtime);

        PageBean<Bag_file> pageBean = bagFileService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }

    /**
     * 跳转添加管理员页面
     * @return
     */
    @GetMapping("/addFileBag")
    public String addAdmin(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Bag_file bag_file = bagFileService.selectById(id);
            model.addAttribute("bag_file",bag_file);
        }
        return "manager/file_manager/addFileBag";
    }
    /**
     * 添加 修改
     * @param bag_file
     * @param status
     * @return
     */
    @PostMapping("/addFileBag")
    @ResponseBody
    public AjaxResult submitAddBadFile(Bag_file bag_file, String status){
        if(bag_file.getBid() !=null){
            //修改
            if(bag_file.getCreattime() != null && bag_file.getCreattime().after(bag_file.getEndtime())){
                ajaxResult.ajaxFalse("创建日期不能晚于结束日期");
                return ajaxResult;
            }
            int count = bagFileService.editByBagFile(bag_file);
            if(count > 0){
                ajaxResult.ajaxTrue("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            //添加
            if(bag_file.getCreattime() != null && bag_file.getCreattime().after(bag_file.getEndtime())){
                ajaxResult.ajaxFalse("创建日期不能晚于结束日期");
                return ajaxResult;
            }
            //添加函数
            int count = bagFileService.insertBagFile(bag_file);
            if(count > 0){
                ajaxResult.ajaxTrue("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }

    /**
     * 删除管理员
     * @param data
     * @return
     */
    @PostMapping("/delBagFile")
    @ResponseBody
    public AjaxResult delAdmin(Data data){
        int count = bagFileService.delByBagFileIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxTrue("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }
}
