package com.zero.system.service.Impl;

import com.zero.system.mapper.FileMapper;
import com.zero.system.model.Files;
import com.zero.system.service.FileService;
import com.zero.system.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public PageBean<Files> queryPage(Map<String, Object> paramMap) {
        PageBean<Files> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Files> datas = fileMapper.queryList(paramMap);
        pageBean.setDatas(datas);   //查询的数据

        Integer totalsize = fileMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    public int insert(Files file) {
        fileMapper.insertFile(file);
        Files files = fileMapper.selectByName(file.getFname());
        file.setFid(files.getFid());
        return fileMapper.insertFile_BagFile(file);
    }

    @Override
    public int delByFileIds(List<Integer> ids) {
        for (Integer fid:ids
             ) {
            Files files= fileMapper.selectById(fid);
            File file= new File(files.getUrl());
            file.delete();
        }
        fileMapper.file_bagfile(ids);
        return fileMapper.delByFileIds(ids);
    }

    @Override
    public Files queryFnameByFid(Integer fid) {
        return fileMapper.selectById(fid);
    }
}
