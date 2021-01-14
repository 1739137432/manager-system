package com.zero.system.controller;

import com.zero.system.model.Admin;
import com.zero.system.service.AdminService;
import com.zero.system.service.RoleService;
import com.zero.system.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname AdminController
 * @Description None
 * @Date 2019/7/17 17:00
 * @Created by WDD
 */
@Controller
@RequestMapping("/manager")
public class AdminController {

    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 跳转管理员页面
     * @return
     */
    @GetMapping("/admin")
    public String admin(){
        return "manager/admin/adminList";
    }

    /**
     * 异步加载管理员列表——搜索功能
     * @param pageno
     * @param pagesize
     * @param username
     * @param phone
     * @param email
     * @return
     */
    @RequestMapping("/adminList")
    @ResponseBody
    public Object adminList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                                @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                                String username,String phone,String email,String rid){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(username)) paramMap.put("username",username);
        if(!StringUtils.isEmpty(phone)) paramMap.put("phone",phone);
        if(!StringUtils.isEmpty(email)) paramMap.put("email",email);
        if(!StringUtils.isEmpty(rid) && !rid.equals("0")) paramMap.put("rid",rid);

        PageBean<Admin> pageBean = adminService.queryPage(paramMap);

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
    @GetMapping("/addAdmin")
    public String addAdmin(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Admin admin = adminService.selectById(id);
            model.addAttribute(Const.ADMIN,admin);
        }
        return "manager/admin/addAdmin";
    }

    /**
     * 添加管理员  修改管理员
     * @param admin
     * @param status
     * @return
     */
    @PostMapping("/addAdmin")
    @ResponseBody
    public AjaxResult submitAddAdmin(Admin admin,String status){
        //添加时：数据库查找有没有该用户名，邮箱
        //修改时：数据库查询有没有别的用户已经使用该名字或邮箱
        Admin byName = adminService.selectByName(admin.getUsername());
        Admin byEmail = adminService.selectByEmail(admin.getEmail());
        if(admin.getRid().equals(0)){
            ajaxResult.ajaxFalse("请选择角色");
            return ajaxResult;
        }
        //on 表示通过 null 表示待审核
        admin.setStatus(status != null?1:0);
        if(admin.getId() !=null){
            //修改管理员
            if(byName != null && !byName.getId().equals(admin.getId())){
                //与修改用户名一样，但存在数据库中，表示后来改的用户名已存在
                ajaxResult.ajaxFalse("用户名已存在");
                return ajaxResult;
            }
            if(byEmail != null && !byEmail.getId().equals(admin.getId())){
                //与修改邮箱一样，但存在数据库中，表示后来改的邮箱已存在
                ajaxResult.ajaxFalse("邮箱已存在");
                return ajaxResult;
            }
            int count = adminService.editByAdmin(admin);
            if(count > 0){
                ajaxResult.ajaxTrue("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            //添加管理员
            if(byName != null){
                //与新加入用户名已经存在数据库中，表示用户名已存在，不可使用
                ajaxResult.ajaxFalse("用户名已存在");
                return ajaxResult;
            }
            if(byEmail != null){
                //与新加入邮箱已经存在数据库中，表示该邮箱已存在，不可使用
                ajaxResult.ajaxFalse("邮箱已存在");
                return ajaxResult;
            }
            //将日期转化为字符串
            String stringDate = DateUtil.getStringDate("yyyy-MM-dd");
            admin.setCreatetime(stringDate);
            //后台添加新的管理员时没有加入密码设置，默认密码为“123”，后面用户登录后可以自行修改
            admin.setPassword(passwordEncoder.encode("123")); // 设置默认密码
            //添加函数
            int count = adminService.insertAdmin(admin);
            if(count > 0){
                ajaxResult.ajaxTrue("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }
    @PostMapping("/zhuce")
    @ResponseBody
    public AjaxResult zhuce(Admin admin){
        //添加时：数据库查找有没有该用户名，邮箱
        //修改时：数据库查询有没有别的用户已经使用该名字或邮箱
        Admin byName = adminService.selectByName(admin.getUsername());
        Admin byEmail = adminService.selectByEmail(admin.getEmail());
        //on 表示通过 null 表示待审核
        admin.setStatus(0);
        admin.setRid(3);
            //添加管理员
            if(byName != null){
                //与新加入用户名已经存在数据库中，表示用户名已存在，不可使用
                ajaxResult.ajaxFalse("用户名已存在");
                return ajaxResult;
            }
            if(byEmail != null){
                //与新加入邮箱已经存在数据库中，表示该邮箱已存在，不可使用
                ajaxResult.ajaxFalse("邮箱已存在");
                return ajaxResult;
            }
            //将日期转化为字符串
            String stringDate = DateUtil.getStringDate("yyyy-MM-dd");
            admin.setCreatetime(stringDate);
            //后台添加新的管理员时没有加入密码设置，默认密码为“123”，后面用户登录后可以自行修改
            admin.setPassword(passwordEncoder.encode("123")); // 设置默认密码
            //添加函数
            int count = adminService.insertAdmin(admin);
            if(count > 0){
                ajaxResult.ajaxTrue("注册成功");
            }else{
                ajaxResult.ajaxFalse("注册失败");
            }
        return ajaxResult;
    }
    /**
     * 删除管理员
     * @param data
     * @return
     */
    @PostMapping("/delAdmin")
    @ResponseBody
    public AjaxResult delAdmin(Data data){
        int count = adminService.delByAdminIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxTrue("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

}
