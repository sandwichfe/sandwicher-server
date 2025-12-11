package com.lww.littlenote.req;

import com.lww.common.web.vo.PageVo;
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

    private Long groupId;

    private Long userId;

}
