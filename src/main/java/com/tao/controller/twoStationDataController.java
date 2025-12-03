package com.tao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tao.mapper.twoStaionMapper;
import com.tao.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class twoStationDataController {

    private final ObjectMapper objectMapper = new ObjectMapper();

@Autowired
private twoStaionMapper tsm;


    @PostMapping("/addTwoStationData")
    public SysResult addTwoStationData(@RequestBody twoStation ts) {
        String itemsJson;
        String stationsJson;
        try {
            itemsJson=objectMapper.writeValueAsString(ts.getItems());
            stationsJson = objectMapper.writeValueAsString(ts.getStationNums());

        twoStationForAdd tsfa = new twoStationForAdd();

        tsfa.setItems(itemsJson);
        tsfa.setStationNums(stationsJson);
        tsfa.setUploadTime(ts.getUploadTime());
        tsfa.setId(ts.getId());
        tsfa.setAreaCode(ts.getAreaCode());
        twoStationForAdd check= tsm.getByIdAndUploadTime(tsfa.getId(), tsfa.getUploadTime(),tsfa.getAreaCode());
        if (check==null){
            tsm.insertTwoStation(tsfa);
            return SysResult.success("添加成功");
        }else {
            tsm.updateTwoStation(tsfa);
            return SysResult.success("更新成功");
        }

        }catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }
    }

    @GetMapping("/getTwoStationData")
    public SysResult getTwoStationData(@RequestParam Integer id,
                                      @RequestParam String uploadTime,@RequestParam String areaCode) {
        twoStationForAdd tsfa=tsm.getByIdAndUploadTime(id,uploadTime,areaCode);
        twoStation ts =new twoStation();
        try{
            List<Map<String, Object>> Items = objectMapper.readValue(tsfa.getItems(), List.class);
            List<String> StationNums = objectMapper.readValue(tsfa.getStationNums(), List.class);

            ts.setItems(Items);
            ts.setStationNums(StationNums);
            ts.setUploadTime(tsfa.getUploadTime());
            ts.setId(tsfa.getId());
            ts.setAreaCode(tsfa.getAreaCode());
            return SysResult.success(ts);
        } catch (Exception e) {
            return SysResult.success("查询异常",null);
        }

    }
}
