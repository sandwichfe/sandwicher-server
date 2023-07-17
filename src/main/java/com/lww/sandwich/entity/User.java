package com.lww.sandwich.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 用户信息
 * @author lww
 * @since 2022/7/20 15:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
