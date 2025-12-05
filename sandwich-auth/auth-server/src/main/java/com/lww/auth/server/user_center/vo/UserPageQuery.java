package com.lww.auth.server.user_center.vo;

import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lww
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户分页查询对象")
public class UserPageQuery extends PageVo {
    
    @Schema(description = "部门ID")
    private Long deptId;
}
