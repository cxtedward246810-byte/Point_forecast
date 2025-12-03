package com.tao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tao.mapper.RecordsMapper;
import com.tao.pojo.Records;
import com.tao.pojo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecordsService {

    @Autowired
    private RecordsMapper recordsMapper;

    // 插入
    public SysResult addRecord(Records record) {
        try {
            recordsMapper.insertRecord(record);
            return SysResult.success("添加成功",record);
        }catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }
    }

    // 删除
    public SysResult removeRecord(Integer id) {
        try {
           recordsMapper.deleteRecord(id);
            return SysResult.success("删除成功");
        }catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }

    }

    // 更新
    public SysResult modifyRecord(Records record) {
        try {
            Integer taskStatus=recordsMapper.getTaskStatus(record.getId());
            if (taskStatus==2){
                return SysResult.success("已发布，无权执行更新",null);
            }
           recordsMapper.updateRecord(record);
            return SysResult.success("更新成功");
        } catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }
    }
//    public List<Records> getUnfinishedByTeam(String teamCode) {
//        // 1. 用 LIKE 初步筛选
//        List<Records> roughList = recordsMapper.findRoughUnfinishedByTeam(teamCode);
//
//        // 2. 再严格过滤
//        return roughList.stream()
//                .filter(record -> hasTeamUnfinished(record.getTeam(), teamCode))
//                .collect(Collectors.toList());
//    }
//
//    private boolean hasTeamUnfinished(String teamField, String teamCode) {
//        if (teamField == null || teamField.isEmpty()) {
//            return false;
//        }
//        String[] parts = teamField.split(",");
//        for (int i = 0; i < parts.length - 1; i += 2) {
//            if (parts[i].equals(teamCode) && "0".equals(parts[i + 1])) {
//                return true;
//            }
//        }
//        return false;
//    }
    // 查询所有
public SysResult getAll(String taskName, String startTime, String endTime,String productType,  int pageNum, int pageSize,String areaCode) {
    try {
        int offset = (pageNum - 1) * pageSize;

        //  在 Java 层预处理 taskName 参数，保证 SQL 正确
        if (taskName != null && !taskName.isEmpty()) {
            taskName = "%" + taskName + "%";
        }
        String teamLike="";
        if (areaCode != null && !areaCode.isEmpty()) {
            teamLike = "%" + areaCode + "%";
        }

        int total = recordsMapper.getTotalCount(taskName, startTime, endTime, productType,areaCode,teamLike);
        int statusOneTotal=recordsMapper.getStatusOneCount(taskName,startTime,endTime,productType,areaCode,teamLike);
        List<Records> list = recordsMapper.getAllRecords(taskName, startTime, endTime,productType, offset, pageSize,areaCode,teamLike);

        Map<String,Object> result = new HashMap<>();
        result.put("totalCounts",total);
        result.put("statusNumOne",statusOneTotal);
        result.put("records",list);
        return SysResult.success(result);

    } catch (Exception e) {
        return SysResult.fail(e.getMessage());
    }
}

    public SysResult getByManageId(Integer manageId) {
        try {
            return SysResult.success(recordsMapper.getByManageId(manageId));
        } catch (Exception e) {
            return SysResult.fail(e.getMessage());
        }
    }

    public SysResult getTaskList(Integer id) throws JsonProcessingException {

        Records record = recordsMapper.getAllRecordsById(id);
        if (record == null) {
            return SysResult.success("没有id为" + id + "的任务", null);
        }

        String jsonArrayStr = record.getTeam();
        if (jsonArrayStr == null || jsonArrayStr.isEmpty()) {
            return SysResult.success("team字段为空", null);
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root = (ArrayNode) mapper.readTree(jsonArrayStr);

        if (root.isEmpty()) {
            return SysResult.success("存储的是一个空数组！", null);
        }

        // LinkedHashMap 保留顺序
        Map<String, JsonNode> normalMap = new LinkedHashMap<>();
        Map<String, JsonNode> creatorMap = new LinkedHashMap<>();

        for (JsonNode obj : root) {
            if (obj.has("includedCity") && obj.get("includedCity").isArray()) {
                ArrayNode includedCity = (ArrayNode) obj.get("includedCity");
                boolean hasCreator = obj.has("creator");

                for (JsonNode node : includedCity) {
                    if (node.has("areaCode")) {
                        String key = node.get("areaCode").asText();
                        if (hasCreator) {
                            // 仅对有 creator 的对象去重
                            creatorMap.putIfAbsent(key, node);
                        } else {
                            // 普通对象去重
                            normalMap.putIfAbsent(key, node);
                        }
                    }
                }
            }
        }

        // 构建最终的数组（先普通对象，再 creator 对象）
        ArrayNode resultArray = mapper.createArrayNode();
        normalMap.values().forEach(resultArray::add);
        creatorMap.values().forEach(resultArray::add);

        return SysResult.success(mapper.writeValueAsString(resultArray));
    }


    public SysResult updateTeam(Integer id,String areaCode, boolean status) throws Exception {
        Records record= recordsMapper.getAllRecordsById(id);
        String jsonArrayStr=record.getTeam();
        String updat= updateTeamStatus(jsonArrayStr,areaCode,status);
        Records rd =new Records();
        rd.setId(id);
        rd.setTeam(updat);
        recordsMapper.updateRecord(rd);
        return SysResult.success("更新成功",updat);
    }

    /**
     * 更新 team JSON 中 includedCity 的 status
     * @param teamJson  数据库取出的 team JSON 字符串
     * @param areaCode  前端传来的 areaCode
     * @param status    前端传来的 status
     * @return 修改后的 JSON 字符串
     * @throws Exception
     */

    private static final ObjectMapper mapper2 = new ObjectMapper();
    public static String updateTeamStatus(String teamJson, String areaCode, boolean status) throws Exception {
        // 解析 JSON 数组
        ArrayNode root = (ArrayNode) mapper2.readTree(teamJson);

        // 当前时间字符串
        String now = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 遍历整个数组
        for (JsonNode obj : root) {
            if (obj.has("includedCity")) {
                ArrayNode includedCity = (ArrayNode) obj.get("includedCity");
                for (JsonNode node : includedCity) {
                    if (node.has("areaCode") && areaCode.equals(node.get("areaCode").asText())) {
                        ObjectNode objNode = (ObjectNode) node;
                        objNode.put("status", status);
                        objNode.put("submitTime", now);
                    }
                }
            }
        }

        // 返回修改后的完整 JSON 数组
        return mapper2.writeValueAsString(root);
    }

    public SysResult generateUpdateTime(Integer id, String areaCode) throws JsonProcessingException {
        Records record= recordsMapper.getAllRecordsById(id);
        String jsonArrayStr=record.getTeam();
        String updat= updateSaveTimeFF(jsonArrayStr,areaCode);
        Records rd =new Records();
        rd.setId(id);
        rd.setTeam(updat);
        recordsMapper.updateRecord(rd);
        return SysResult.success("更新成功",updat);
    }

    private String updateSaveTimeFF(String teamJson, String areaCode) throws JsonProcessingException {
        // 解析 JSON 数组
        ArrayNode root = (ArrayNode) mapper2.readTree(teamJson);

        // 当前时间字符串
        String now = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 遍历整个数组
        for (JsonNode obj : root) {
            if (obj.has("includedCity")) {
                ArrayNode includedCity = (ArrayNode) obj.get("includedCity");
                for (JsonNode node : includedCity) {
                    if (node.has("areaCode") && areaCode.equals(node.get("areaCode").asText())) {
                        ObjectNode objNode = (ObjectNode) node;
                        objNode.put("updateTime", now);
                    }
                }
            }
        }

        // 返回修改后的完整 JSON 数组
        return mapper2.writeValueAsString(root);
    }
}
