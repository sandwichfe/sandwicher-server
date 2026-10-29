package com.lww.littlenote.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 笔记数量统计VO
 *
 * @author lww
 */
@Data
public class NoteCountVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "笔记总数量")
    private Long totalCount;

    @Schema(description = "各分组笔记数量")
    private List<GroupItem> groupCounts;

    /**
     * 单个分组的笔记数量，没有笔记的分组返回 0
     */
    @Data
    public static class GroupItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "分组ID")
        private Long groupId;

        @Schema(description = "分组名称")
        private String groupName;

        @Schema(description = "笔记数量")
        private Long noteCount;
    }
}
