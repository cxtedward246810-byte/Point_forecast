package com.tao.mapper;

import com.tao.pojo.ManageForAdd;
import com.tao.pojo.Records;
import com.tao.pojo.twoStation;
import com.tao.pojo.twoStationForAdd;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface twoStaionMapper {

    // 🔹 插入记录（id 自增）
    @Insert("INSERT INTO T_DATA_2STATION (ID,ITEMS, STATIONNUMS," +
            " UPLOADTIME,AREACODE) " +
            "VALUES (#{id},#{items}, #{stationNums}," +
            " #{uploadTime},#{areaCode})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTwoStation(twoStationForAdd tsfa);


    @Select("SELECT * FROM T_DATA_2STATION WHERE " +
            "ID=#{id} AND  UPLOADTIME=#{uploadTime} AND AREACODE=#{areaCode}")
    twoStationForAdd getByIdAndUploadTime(Integer id, String uploadTime,String areaCode);

    @Update("<script>" +
            "UPDATE T_DATA_2STATION SET " +
            "<trim suffixOverrides=','>" +
            "<if test='items != null'>ITEMS = #{items},</if>" +
            "<if test='stationNums != null'>STATIONNUMS = #{stationNums}</if>" +
            "</trim>" +
            "WHERE ID = #{id} AND UPLOADTIME=#{uploadTime} AND AREACODE=#{areaCode}" +
            "</script>")
    void updateTwoStation(twoStationForAdd tsfa);
}
