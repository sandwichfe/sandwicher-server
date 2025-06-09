package com.lww.oss.entity;


import lombok.Data;

/**
 * 文件类
 *
 *
 * @author lww
 */
@Data
public class FileInfo {

    private String fileName;

    private Boolean directoryFlag;

    private String etag;

}

