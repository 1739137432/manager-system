package com.zero.system.controller;

import com.zero.system.model.Files;
import com.zero.system.service.FileService;
import com.zero.system.util.AjaxResult;
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

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

/**
 * @ClassName: FileUploadController
 * @Author: xz
 * @CreateDate: 2019/3/25 17:31
 * @Version: 1.0
 * 上传如果超过10m的文件会报错，最好修改tomcat的，如果使用js对图片要求不是太高可以进行图片压缩
 */
@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private AjaxResult ajaxResult;
    String bid;
    //文件路径
//    String path = "D:\\datafile\\";
    //String path = "D:\\status_study\\manager-system\\src\\main\\resources\\upload";
   // String path = ".\\manager-system\\src\\main\\resources\\upload";
    /**
     * 跳转上传单文件页面
     * @return
     */
    @RequestMapping("/upload")
    public String singleFile(String bid, Model model) {
        model.addAttribute("bid",bid);
        this.bid=bid;
        return "/upload";
    }

    /**
     * 跳转上传多文件页面
     * @return
     */
    @RequestMapping("/uploadBatch")
    public String multipleFiles() {
        return "/uploadBatch";
    }


    /**
     * 上传单文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        //判断非空
        if (file.isEmpty()) {
            //return "上传的文件不能为空";
            ajaxResult.ajaxFalse("上传的文件不能为空");
            return ajaxResult;
        }
        //上传文件的位置,默认会在项目根目录找static文件夹,需手动创建,不然找到是临时路径。
        String path = request.getSession().getServletContext().getRealPath("/upload/");
        try {
            // 测试MultipartFile接口的各个方法
            logger.info("[文件类型ContentType] - [{}]",file.getContentType());
            logger.info("[文件组件名称Name] - [{}]",file.getName());
            logger.info("[文件原名称OriginalFileName] - [{}]",file.getOriginalFilename());
            logger.info("[文件大小] - [{}]",file.getSize());
            logger.info(this.getClass().getName()+"图片路径："+path);
            File f = new File(path);
            // 如果不存在该路径就创建
            if (!f.exists()) {
                f.mkdir();
            }
            File dir = new File(path + file.getOriginalFilename());
            // 文件写入
            file.transferTo(dir);
            // 这里除了transferTo方法，也可以用字节流的方式上传文件，但是字节流比较慢，所以还是建议用transferTo
//            writeFile(file);
            Files files=new Files();
            files.setFname(file.getOriginalFilename());
            files.setFtype(file.getContentType());
            files.setFsize(file.getSize());
            files.setUrl(path + file.getOriginalFilename());
            files.setBid(Integer.parseInt(bid));
            submitAddFile(files);
            //return "上传单个文件成功";
            ajaxResult.ajaxFalse("上传单个文件成功");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
           // return "上传单个文件失败";
            ajaxResult.ajaxFalse("上传单个文件失败");
            return ajaxResult;
        }
    }

    /**
     * 使用字节流形式进行写文件
     * @param file
     */
    public void writeFile(MultipartFile file,String path) {
        try {
            //获取输出流
            OutputStream os = new FileOutputStream(path + file.getOriginalFilename());
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            InputStream is = file.getInputStream();
            byte[] buffer = new byte[1024];
            //判断输入流中的数据是否已经读完的标识
            int length = 0;
            //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while((length = is.read(buffer))!=-1){
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 多文件上传
     * @param files
     * @return
     */
    @PostMapping("/uploadBatch")
    @ResponseBody
    public String uploadBatch(@RequestParam("files") MultipartFile[] files,String filePath) {
        logger.info("文件名称："+ files );
        if(files!=null&&files.length>0){
            //String filePath = "D:\\datafile\\";
            for (MultipartFile mf : files) {
                // 获取文件名称
                String fileName = mf.getOriginalFilename();
                // 获取文件后缀
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                // 重新生成文件名
                fileName = UUID.randomUUID()+suffixName;

                if (mf.isEmpty()) {
                    return "文件名称："+ fileName +"上传失败，原因是文件为空!";
                }
                File dir = new File(filePath + fileName);
                try {
                    // 写入文件
                    mf.transferTo(dir);
                    logger.info("文件名称："+ fileName +"上传成功");
                } catch (IOException e) {
                    logger.error(e.toString(), e);
                    return "文件名称："+ fileName +"上传失败";
                }
            }
            return "多文件上传成功";
        }
        return "上传文件不能为空";
    }

    public int submitAddFile(Files file){
        return fileService.insert(file);
    }

}
