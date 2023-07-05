package com.pxxy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer uId;
    private String uName;
    //权限路径,权限详情
    private Map<String, PermissionDTO> permission;
}
