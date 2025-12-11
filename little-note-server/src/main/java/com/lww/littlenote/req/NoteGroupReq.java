package com.lww.littlenote.req;

import lombok.Data;
import java.io.Serializable;

@Data
public class NoteGroupReq implements Serializable {
    private Long id;
    private String groupName;
}
