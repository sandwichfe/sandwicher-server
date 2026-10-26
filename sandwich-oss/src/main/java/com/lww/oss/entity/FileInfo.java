package com.lww.oss.entity;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件类
 *
 *
 * @author lww
 */
@Data
public class FileInfo {

    private String fileName;

    /**
     * 是否为目录，statObject 获取单个对象元数据时恒为 false
     */
    private Boolean directoryFlag;

    private String etag;

    /**
     * 文件大小，单位字节；列表与元数据场景均返回
     */
    private Long size;

    /**
     * 最后修改时间；列表与元数据场景均返回
     */
    private LocalDateTime lastModified;

    /**
     * 文件类型，仅元数据场景返回，列表场景为 null
     */
    private String contentType;

}
