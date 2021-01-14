package com.zero.system.service;

import com.zero.system.model.Files;
import com.zero.system.util.PageBean;

import java.util.List;
import java.util.Map;

public interface FileService {
    PageBean<Files> queryPage(Map<String, Object> paramMap);

    int insert(Files file);

    int delByFileIds(List<Integer> ids);


    Files queryFnameByFid(Integer fid);
}
