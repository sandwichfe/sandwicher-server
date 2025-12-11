package com.lww.auth.server.user_center.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Schema(description = "用户VO")
public class UserVo implements Serializable {
    @Schema(description = "自增id")
    private Long id;

    @Schema(description = "用户名、昵称")
    private String nickname;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "部门ID列表")
    private List<Long> deptIds;

    @Schema(description = "用户来源")
    private String sourceFrom;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
