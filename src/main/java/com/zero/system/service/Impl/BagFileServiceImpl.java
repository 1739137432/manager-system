package com.zero.system.service.Impl;

import com.zero.system.mapper.BagFileMapper;
import com.zero.system.mapper.FileMapper;
import com.zero.system.model.Bag_file;
import com.zero.system.model.Files;
import com.zero.system.service.BagFileService;
import com.zero.system.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class BagFileServiceImpl implements BagFileService {

    @Autowired
    private BagFileMapper bagFileMapper;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public PageBean<Bag_file> queryPage(Map<String, Object> paramMap) {
        PageBean<Bag_file> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Bag_file> datas = bagFileMapper.queryList(paramMap);
        pageBean.setDatas(datas);   //查询的数据

        Integer totalsize = bagFileMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    public Bag_file selectById(Integer id) {
        return bagFileMapper.selectById(id);
    }

    @Override
    public int editByBagFile(Bag_file bag_file) {
        return bagFileMapper.editByBagFile(bag_file);
    }

    @Override
    public int insertBagFile(Bag_file bag_file) {
        return bagFileMapper.insertBagFile(bag_file);
    }

    @Override
    public int delByBagFileIds(List<Integer> ids) {
        //删除包中的文件
        for (Integer id:ids){
           Files files= fileMapper.selectById(id);
            File file = new File(files.getUrl());
            file.delete();
            fileMapper.delFileByBid(id);
        }
        //删除中间表
        bagFileMapper.delByFile_BagFile(ids);
        //删除包
        return bagFileMapper.delByBagFileIds(ids);
    }

    @Override
    public int queryChirdFile(Integer id) {
        return bagFileMapper.queryChirdFile(id);
    }
}
