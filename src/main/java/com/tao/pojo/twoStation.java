package com.tao.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class twoStation {
    private List<Map<String, Object>> items;
    private List<String> stationNums;
    private String uploadTime;
    private Integer id;
    private String areaCode;
}
