package com.tao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tao.mapper.RecordsMapper;
import com.tao.mapper.StationLonLatMapper;
import com.tao.pojo.*;
import com.tao.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/PointForecastRecords")
public class RecordsController {
@Autowired
private RecordsMapper mapper;
    @Autowired
    private RecordsService recordsService;
@Autowired
private StationLonLatMapper sllm;


@PostMapping("/getLonLatByStationNum")
public SysResult getLonLatByStationNum(@RequestBody  List<String> stationNums) {
    try {
        List<StaionLatlon> result = new ArrayList<>();
        for (String stationNum:stationNums ){
           result.add(sllm.getLatAndLon(stationNum));
        }
        return SysResult.success(result);
    } catch (Exception e) {
        return SysResult.fail(e.getMessage());
    }
}
    @PostMapping("/addRecord")
    public SysResult addRecord(@RequestBody Records record) {
        try {
            return recordsService.addRecord(record);
        } catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }
    }

//    @GetMapping("/oneTeamWork")
//    public SysResult oneTeamWork(String areaCode) {
//            try {
//                List<Records> result =  recordsService.getUnfinishedByTeam(areaCode);
//                if (result.isEmpty()){
//                    return SysResult.success("该areaCode下没有任务",null);
//                }else {
//                    return SysResult.success("查询到未完成的任务列表!",result);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//    }
    // 🔹 添加记录
//    @PostMapping("/addRecord")
//    public SysResult addRecord(@RequestBody RecordsDTO record) {
//
//        try {
//            Records records = new Records();
//            records.setStartReportTime(record.getStartReportTime());
//            records.setManageId(record.getManageId());
//            records.setMakeTime(record.getMakeTime());
//            List<DATAS> datas = record.getDatas();
//            for (DATAS d : datas) {
//                records.setData(d.getData());
//                records.setElement(d.getElement());
//                records.setHourSpan(d.getHourSpan());
//                records.setIsUpdate(d.getIsUpdate());
//                records.setUserName(d.getUserName());
//                records.setStation(d.getStation());
//                recordsService.addRecord(records);
//            }
//            return SysResult.success("添加成功");
//        } catch (Exception e) {
//            return SysResult.fail(e.getMessage());
//        }
//
//    }

    // 🔹 删除记录（DELETE）
    @GetMapping("/deleteRecord")
    public SysResult deleteRecord(@RequestParam Integer id) {
        return recordsService.removeRecord(id);
    }

    // 🔹 更新记录
    @PostMapping("/updateRecord")
    public SysResult updateRecord(@RequestBody Records record) {
       return recordsService.modifyRecord(record);
    }

    @GetMapping("/getAll")
    public SysResult getAll(@RequestParam(required = false) String taskName,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime,
                            @RequestParam(required = false) String productType,
                            @RequestParam(required = false) String areaCode,
                            @RequestParam(defaultValue = "1") int pageNum,
                            @RequestParam(defaultValue = "10") int pageSize) {
        return recordsService.getAll(taskName, startTime, endTime,  productType,pageNum, pageSize,areaCode);
    }

    @GetMapping("/checkTaskNameExist")
    public SysResult getTaskName(String taskName) {
       try {
           List<Records> rc=mapper.check(taskName);
           if (rc!=null&& !rc.isEmpty()){
               return SysResult.success(true);
           }else {
               return SysResult.success(false);
           }
       } catch (Exception e) {
           return SysResult.fail(e.getMessage());
       }
    }

    @GetMapping("/getTaskListAll")
    public SysResult getTaskListAll(Integer id) throws JsonProcessingException {
        return recordsService.getTaskList(id);
    }

    @GetMapping("/getTaskById")
    public SysResult getTaskById(@RequestParam Integer id) {
    try {
        return SysResult.success(mapper.getAllRecordsById(id));
    }catch (Exception e) {
        return SysResult.fail(e.getMessage());
    }

    }
    @GetMapping("/updateTeamStatus")
    public SysResult updateTeamStatus(@RequestParam Integer id,@RequestParam Boolean status,@RequestParam String areaCode,@RequestParam(required = false) String showName) {
    try {
        return  recordsService.updateTeam(id,areaCode,status,showName);
    } catch (Exception e) {
        return SysResult.fail(e.getMessage());
    }
    }

    @GetMapping("/generateUpdateTime")
    public SysResult updateSaveTime(@RequestParam Integer id,@RequestParam String areaCode) {
        try {
            return  recordsService.generateUpdateTime(id,areaCode);
        } catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }
    }

    @GetMapping("/getByManageId")
    public SysResult getByManageId(@RequestParam Integer manageId) {
        return recordsService.getByManageId(manageId);
    }
}
