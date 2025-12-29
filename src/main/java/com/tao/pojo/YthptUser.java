package com.tao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YthptUser {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String userInfo;
    private String areaCode; //行政区划代码
    private String showName;
    private String departmentId;
}
