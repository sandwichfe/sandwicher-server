package com.lww.littlenote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class NoteReq implements Serializable {

    @Schema(description = "笔记ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "分组ID")
    private Long groupId;
}
