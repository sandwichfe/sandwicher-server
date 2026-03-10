package com.lww.littlenote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
public class NoteGroupReq implements Serializable {

    @Schema(description = "分组ID")
    private Long id;

    @Schema(description = "分组名称")
    private String groupName;
}
