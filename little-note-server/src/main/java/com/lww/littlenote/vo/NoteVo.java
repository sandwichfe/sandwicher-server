package com.lww.littlenote.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lww
 * @since 2024-06-13
 */
@Data
public class NoteVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "分组ID")
    private Long groupId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 标题高亮内容，仅搜索结果中可能有值。
     */
    @Schema(description = "标题高亮内容")
    private String highlightTitle;

    /**
     * 正文高亮片段，仅搜索结果中可能有值。
     */
    @Schema(description = "正文高亮片段")
    private String highlightContent;

}
