package com.lww.littlenote.entity.es;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ES 中的笔记明文索引副本。
 */
@Data
public class NoteEsDocument {

    private Long id;

    private String title;

    private String content;

    private Long groupId;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
