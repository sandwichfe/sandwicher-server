package com.lww.littlenote.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    /**
     * 列表接口只下发 summary，正文不下发，因此为空时不序列化该字段。
     */
    @Schema(description = "内容（仅详情接口返回）")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    @Schema(description = "内容摘要，正文前200个字")
    private String summary;

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

    /**
     * 摘要截取的最大字数。
     */
    private static final int SUMMARY_MAX_LENGTH = 200;

    /**
     * 截取正文前 200 个字作为列表摘要。
     *
     * @param content 正文
     * @return 摘要，正文为空时返回 null
     */
    public static String summaryOf(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        if (content.length() <= SUMMARY_MAX_LENGTH) {
            return content;
        }
        // 避免在代理对中间截断，保证 emoji 等字符完整。
        int end = SUMMARY_MAX_LENGTH;
        if (Character.isHighSurrogate(content.charAt(end - 1))) {
            end--;
        }
        return content.substring(0, end);
    }

}
