package com.tao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tao.pojo.Manage;
import com.tao.pojo.ManageForAdd;
import com.tao.pojo.SysResult;
import com.tao.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/PointForecastManage")
public class ManageController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ManageService manageService;

    // 🔹 添加记录
    @PostMapping("/addManage")
    public SysResult addManage(@RequestBody Manage manage) {
        String elementsJson;
        String stationsJson;
        try {
            elementsJson = objectMapper.writeValueAsString(manage.getElements());
            stationsJson = objectMapper.writeValueAsString(manage.getStations());
        } catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }

        ManageForAdd mfa= new  ManageForAdd();
        mfa.setForecastTime(manage.getForecastTime());
        mfa.setEndTime(manage.getEndTime());
        mfa.setForecastResource(manage.getForecastResource());
        mfa.setMakeTime(manage.getMakeTime());
        mfa.setProductName(manage.getProductName());
        mfa.setStartTime(manage.getStartTime());
        mfa.setElements(elementsJson);
        mfa.setStations(stationsJson);
        return manageService.addManage(mfa);
    }

    // 🔹 删除记录
    @GetMapping("/deleteManage")
    public SysResult deleteManage(@RequestParam Integer id) {
        return manageService.removeManage(id);
    }

    // 🔹 更新记录
    @PostMapping("/updateManage")
    public SysResult updateManage(@RequestBody Manage manage) {
        String elementsJson;
        String stationsJson;
        try {
            elementsJson = objectMapper.writeValueAsString(manage.getElements());
            stationsJson = objectMapper.writeValueAsString(manage.getStations());
        } catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }

        ManageForAdd mfa= new  ManageForAdd();
        mfa.setForecastTime(manage.getForecastTime());
        mfa.setId(manage.getId());
        mfa.setEndTime(manage.getEndTime());
        mfa.setForecastResource(manage.getForecastResource());
        mfa.setMakeTime(manage.getMakeTime());
        mfa.setProductName(manage.getProductName());
        mfa.setStartTime(manage.getStartTime());
        mfa.setElements(elementsJson);
        mfa.setStations(stationsJson);
        return manageService.modifyManage(mfa);
    }

    // 🔹 查询所有记录
    @GetMapping("/getAllManage")
    public SysResult getAllManage() {
        return manageService.getAll();
    }

}
