package com.pxxy.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.utils.TokenUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

/**
 * Class name: OnlineUserVO
 * Create time: 2023/11/18 16:26
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("在线用户信息")
public class OnlineUserVO {

    @ApiModelProperty("令牌信息")
    private Token token;

    @ApiModelProperty("用户信息")
    private User user;

    @Getter
    @ApiModel("令牌信息")
    @AllArgsConstructor
    public static class Token {

        @ApiModelProperty("令牌编号")
        private String token;

        @ApiModelProperty("到期时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expired;

    }

    @Getter
    @ApiModel("用户信息")
    @AllArgsConstructor
    public static class User {

        @ApiModelProperty("用户编号")
        private Integer userId;

        @ApiModelProperty("用户名")
        private String userName;

    }

    public OnlineUserVO(Map.Entry<TokenUtil.Token, UserDTO> entry) {
        this.token = new Token(entry.getKey().token, new Date(entry.getKey().getDeadTime()));
        this.user = new User(entry.getValue().getUId(), entry.getValue().getUName());
    }

    @Override
    public String toString() {
        return token.token + "," + user.userId;
    }

}
