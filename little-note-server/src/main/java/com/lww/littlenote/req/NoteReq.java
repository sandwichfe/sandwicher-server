package com.lww.littlenote.req;

import lombok.Data;
import java.io.Serializable;

@Data
public class NoteReq implements Serializable {
    private Long id;
    private String title;
    private String content;
    private Long groupId;
}
