package com.lww.sandwich.entity;

import lombok.Data;

/**
 * @author lww
 * @description: user
 * @date 2022/3/10 11:09
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
