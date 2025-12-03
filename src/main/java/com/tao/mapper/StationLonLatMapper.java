package com.tao.mapper;


import com.tao.pojo.StaionLatlon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;



@Mapper
public interface StationLonLatMapper {
    @Select("SELECT STATION_ID_C as station ,LON,LAT FROM STATION_TEST WHERE STATION_ID_C=#{station}")
    StaionLatlon getLatAndLon(String station);
}
