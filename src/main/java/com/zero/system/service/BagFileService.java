package com.zero.system.service;

import com.zero.system.model.Bag_file;
import com.zero.system.util.PageBean;

import java.util.List;
import java.util.Map;

public interface BagFileService {
    PageBean<Bag_file> queryPage(Map<String,Object> paramMap);

    Bag_file selectById(Integer id);


    int editByBagFile(Bag_file bag_file);

    int insertBagFile(Bag_file bag_file);

    int delByBagFileIds( List<Integer> bid);

    int queryChirdFile(Integer id);
}
