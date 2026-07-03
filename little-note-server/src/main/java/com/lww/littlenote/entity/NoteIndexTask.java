package com.lww.littlenote.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 笔记 ES 索引同步任务。
 */
@Data
@TableName("note_index_task")
public class NoteIndexTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 需要同步到 ES 的笔记 ID。
     */
    private Long noteId;

    /**
     * 操作类型：CREATE / UPDATE / DELETE。
     */
    private String action;

    /**
     * 任务状态：WAIT / SUCCESS / FAIL。
     */
    private String status;

    /**
     * 已重试次数。
     */
    private Integer retryCount;

    /**
     * 最近一次同步失败原因。
     */
    private String errorMsg;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
