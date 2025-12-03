package com.tao.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RecordsDTO {
    private Integer manageId;
    private String makeTime;
    private String startReportTime;
    private List<DATAS> datas;
}
