package com.tao.mapper;


import com.tao.pojo.YthptUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface YthptUserMapper {

    @Select("select * from t_ythpt_user where id=#{id}")
    YthptUser getUserMsg(Integer id);


    @Select("select userName from t_ythpt_user where areaCode=#{areaCode}")
    List<String> getUserByAreaCode(String areaCode);

}
