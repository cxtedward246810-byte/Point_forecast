package com.tao.mapper;

import com.tao.pojo.Manage;
import com.tao.pojo.ManageForAdd;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ManageMapper {

    // 🔹 插入记录（id 自增）
    @Insert("INSERT INTO T_POINT_MANAGE (STARTTIME, ENDTIME, FORECASTRESOURCE, ELEMENTS, STATIONS," +
            " MAKETIME, PRODUCTNAME,forecastTime) " +
            "VALUES (#{startTime}, #{endTime}, #{forecastResource}, #{elements}, #{stations}," +
            " #{makeTime}, #{productName},#{forecastTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertManage(ManageForAdd manage);

    @Delete("DELETE FROM T_POINT_MANAGE WHERE ID = #{id}")
    void deleteManage(Integer id);


    @Update("<script>" +
            "UPDATE T_POINT_MANAGE SET " +
            "<trim suffixOverrides=','>" +
            "<if test='startTime != null'>STARTTIME = #{startTime},</if>" +
            "<if test='endTime != null'>ENDTIME = #{endTime},</if>" +
            "<if test='forecastResource != null'>FORECASTRESOURCE = #{forecastResource},</if>" +
            "<if test='elements != null'>ELEMENTS = #{elements},</if>" +
            "<if test='stations != null'>STATIONS = #{stations},</if>" +
            "<if test='makeTime != null'>MAKETIME = #{makeTime},</if>" +
            "<if test='productName != null'>PRODUCTNAME = #{productName},</if>" +
            "<if test='forecastTime != null'>forecastTime = #{forecastTime}</if>" +
            "</trim>" +
            "WHERE ID = #{id}" +
            "</script>")
    void updateManage(ManageForAdd manage);

    // 🔹 查询所有记录
    @Select("SELECT * FROM T_POINT_MANAGE")
    List<ManageForAdd> getAllManage();

}
