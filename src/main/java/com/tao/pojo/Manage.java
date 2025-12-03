package com.tao.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Manage {
    private Integer id;
    private String startTime;
    private String endTime;
    private String forecastResource;
    private List<Map<String, Object>> elements;
    private List<String> stations;
    private String makeTime;
    private String productName;
    private String forecastTime;

}
