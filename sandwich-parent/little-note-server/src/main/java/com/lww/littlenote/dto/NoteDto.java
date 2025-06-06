package com.lww.littlenote.dto;

import com.lww.common.web.vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author sandw
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NoteDto extends PageVo {

    private Long groupId;

    private Long userId;

}
