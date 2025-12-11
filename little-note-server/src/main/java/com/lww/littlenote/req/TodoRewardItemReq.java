package com.lww.littlenote.req;

import lombok.Data;
import java.io.Serializable;

@Data
public class TodoRewardItemReq implements Serializable {
    private Long id;
    private String itemName;
    private Integer pointsCost;
    private String description;
    private Integer status;
}
