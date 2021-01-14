package com.zero.system.mapper;

import com.zero.system.model.Bag_file;
import com.zero.system.model.Files;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper {
    List<Files> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    int insertFile(Files bagFlie);

    Files selectById(Integer fid);

    int delByFileIds(List<Integer> ids);

    int editByBagFile(Files bagFlie);

    Files selectByName(String username);

    int insertFile_BagFile(Files file);

    void file_bagfile(List<Integer> ids);

    List<Integer> quertByBid(List<Integer> ids);

    void delFileByBid(Integer id);

    List<Files> quertFileByBids(List<Integer> ids);

    //BagFileMapper selectByEmail(String email);

//    @Select("select * from Bag_file where username = #{username}")
//    @Results({
//            @Result(id = true, property = "id",column = "id"),
//            @Result(property = "roles", column = "id", javaType = List.class,
//                    many = @Many(select = "com.zero.system.mapper.RoleMapper.findByAdminId"))
//    })
//    BagFileMapper findByName(String username);
}
