package com.zero.system.controller;

import com.zero.system.config.FdfsConfig;
import com.zero.system.mapper.FileMapper;
import com.zero.system.model.Files;
import com.zero.system.service.FileService;
import com.zero.system.util.AjaxResult;
import com.zero.system.util.CommonFileUtil;
import com.zero.system.util.Data;
import com.zero.system.util.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/manager")
public class FileController {
    @Autowired
    private CommonFileUtil fileUtil;

    @Autowired
    private FdfsConfig fdfsConfig;

    @Autowired
    private FileService fileService;


    @Autowired
    private AjaxResult ajaxResult;

    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    // 跳转上传页面
    @RequestMapping("/goIndex")
    public String goIndex(){
        logger.info("进入主页面");
        return "/file";
    }



    @RequestMapping("/FileList")
    @ResponseBody
    public Object adminList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            String fname,String ftype,String fsize,String url,String bid){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(fname)) paramMap.put("fname",fname);
        if(!StringUtils.isEmpty(ftype)) paramMap.put("ftype",ftype);
        if(!StringUtils.isEmpty(fsize)) paramMap.put("fsize",fsize);
        if(!StringUtils.isEmpty(url)) paramMap.put("url",url);
        if(!StringUtils.isEmpty(bid)) paramMap.put("bid",bid);

        PageBean<Files> pageBean = fileService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }
    // 跳转上传页面
    @RequestMapping("/addFile")
    public String addFile(){
        return "/manager/file_manager/addFile";
    }

    // 使用fastdfs进行文件上传
    @RequestMapping("/uploadFileToFast")
    public String uoloadFileToFast(@RequestParam("fileName")MultipartFile file, RedirectAttributes attributes) throws IOException{

        if(file.isEmpty()){
            logger.info("文件不存在");
        }
        String path = fileUtil.uploadFile(file);
        String url = fdfsConfig.getResHost()+path;
        attributes.addAttribute("url", url);
        return "redirect:/manager/success";
    }

    // 跳转成功页面
    @RequestMapping("/success")
    public String success(HttpServletRequest request){
        request.setAttribute("imgUrl", request.getParameter("url"));
        logger.info(request.getParameter("url"));
        logger.info("进入上传成功页面");
        return "/success";
    }

    /**
     * 删除管理员
     * @param data
     * @return
     */
    @PostMapping("/delFile")
    @ResponseBody
    public AjaxResult delFile(Data data){
        int count = fileService.delByFileIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxTrue("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

}