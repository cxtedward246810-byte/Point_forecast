package com.tao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tao.mapper.ManageMapper;
import com.tao.pojo.Manage;
import com.tao.pojo.ManageForAdd;
import com.tao.pojo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ManageService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ManageMapper manageMapper;

    // 添加
    public SysResult addManage(ManageForAdd manage) {
        try{
         manageMapper.insertManage(manage);
            return SysResult.success("添加成功");
        } catch (Exception e) {

            return SysResult.fail(e.getMessage());
        }

    }

    // 删除
    public SysResult removeManage(Integer id) {
        try{
            manageMapper.deleteManage(id);
            return SysResult.success("删除成功");
        } catch (Exception e) {

            return SysResult.fail(e.getMessage());
        }
    }

    // 更新
    public SysResult modifyManage(ManageForAdd manage) {
        try{
            manageMapper.updateManage(manage);
            return SysResult.success("更新成功");
        } catch (Exception e) {

            return SysResult.fail(e.getMessage());
        }
    }

    // 查询所有
    public SysResult getAll() {

        try{
            List<Manage> mmaa =new ArrayList<>();

            List<ManageForAdd> MFA=manageMapper.getAllManage();
            for (ManageForAdd mfa : MFA) {
                Manage manage=new Manage();
                try {
                    // 反序列化 elements 和 stations 字段
                    List<Map<String, Object>> elements = objectMapper.readValue(mfa.getElements(), List.class);
                    List<String> stations = objectMapper.readValue(mfa.getStations(), List.class);

                    // 设置反序列化后的值回对象
                    manage.setStartTime(mfa.getStartTime());
                    manage.setEndTime(mfa.getEndTime());
                    manage.setId(mfa.getId());
                    manage.setForecastResource(mfa.getForecastResource());
                    manage.setMakeTime(mfa.getMakeTime());
                    manage.setProductName(mfa.getProductName());
                    manage.setElements(elements);
                    manage.setStations(stations);
                    mmaa.add(manage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return SysResult.success(mmaa);
        } catch (Exception e) {

            return SysResult.fail(e.getMessage());
        }
    }


}
