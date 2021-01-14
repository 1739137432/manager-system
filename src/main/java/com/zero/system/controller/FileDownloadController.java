package com.zero.system.controller;

import com.sun.deploy.net.URLEncoder;
import com.zero.system.model.Files;
import com.zero.system.service.FileService;
import com.zero.system.util.AjaxResult;
import com.zero.system.util.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @ClassName: FileUploadController
 * @Author: xz
 * @CreateDate: 2019/3/25 17:31
 * @Version: 1.0
 * 文件下载
 */
@Controller
@RequestMapping("/manager")
public class FileDownloadController {
    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private FileService fileService;
    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

    @RequestMapping("/download")
    public String singleFile() {
        return "/download";
    }


    @RequestMapping("/downloadfile")
    @ResponseBody
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, Data data) throws UnsupportedEncodingException {
        for (Integer fid:data.getIds()) {
            Files file = fileService.queryFnameByFid(fid);
            downloadFile1(request,response,file.getFname());
        }

    }



    @RequestMapping("/downloadfile1")
    @ResponseBody
    public String downloadFile1(HttpServletRequest request, HttpServletResponse response, String fname) throws UnsupportedEncodingException {


        //fname = "sudoku.png";
//        Files afile = fileService.queryFnameByFid(fid);
        //String fileName = "tomatoes.jpg";
        //设置文件路径
        File file = new File(request.getSession().getServletContext().getRealPath("/upload/")+fname);
        //File file = new File(realPath , fileName);
        logger.info(request.getSession().getServletContext().getRealPath("/upload/")+fname);
        if (file.exists()) {
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment; fileName=" + fname + ";filename*=utf-8''" + URLEncoder.encode(fname, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                logger.info("" + i);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
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
        return "文件不存在";
    }
}

//String fileName = "大话设计模式(带目录完整版).pdf";// 文件名





