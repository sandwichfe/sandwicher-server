package com.lww.littlenote.req;

import com.lww.web.support.vo.PageVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @author sandw
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NoteQueryReq extends PageVo implements Serializable {

    @Schema(description = "分组ID")
    private Long groupId;

    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 搜索关键词，为空时走普通数据库列表查询。
     */
    @Schema(description = "搜索关键词")
    private String keyword;

}
