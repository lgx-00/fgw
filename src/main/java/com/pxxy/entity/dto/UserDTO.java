package com.pxxy.entity.dto;

import com.pxxy.entity.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
public class UserDTO {

    private Integer uId;

    private String uName;

    /**
     * 所属科室 外键，NULL 表示所有权限
     */
    private Integer depId;

    private Integer couId;

    private List<Integer> roleIds;

    //权限路径,权限详情
    private Map<String, PermissionDTO> permission;

    public UserDTO(Integer uid) {
        uId = uid;
    }

    public UserDTO(User user) {
        uId = user.getUId();
        uName = user.getUName();
        depId = user.getDepId();
        couId = user.getCouId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(uId, userDTO.uId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId);
    }
}
