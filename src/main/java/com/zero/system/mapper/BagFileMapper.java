package com.zero.system.mapper;

import com.zero.system.model.Bag_file;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BagFileMapper {
    List<Bag_file> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    int insertBagFile(Bag_file bagFlie);

    Bag_file selectById(Integer id);

    int delByBagFileIds(List<Integer> ids);

    int editByBagFile(Bag_file bagFlie);

    Bag_file selectByName(String username);

    int queryChirdFile(Integer id);

    void delByFile_BagFile(List<Integer> id);

    List<Integer> queryFileId(List<Integer> id);

    //BagFileMapper selectByEmail(String email);

//    @Select("select * from Bag_file where username = #{username}")
//    @Results({
//            @Result(id = true, property = "id",column = "id"),
//            @Result(property = "roles", column = "id", javaType = List.class,
//                    many = @Many(select = "com.zero.system.mapper.RoleMapper.findByAdminId"))
//    })
//    BagFileMapper findByName(String username);
}
