package com.zero.system.controller;

import com.zero.system.model.Bag_file;
import com.zero.system.service.BagFileService;
import com.zero.system.util.AjaxResult;
import com.zero.system.util.Data;
import com.zero.system.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class UserBagFileController {
    @Autowired
    private BagFileService bagFileService;

    @Autowired
    private AjaxResult ajaxResult;


    @GetMapping("/UserbagFile")
    public String admin(){
        return "manager/user_file/UserFileBagList";
    }

    @RequestMapping("/Userbagfilein")
    public String bagfilein(Integer bid, Model modeld){
        modeld.addAttribute("bid",bid);
        return "/manager/user_file/UserFileList";
    }
}
