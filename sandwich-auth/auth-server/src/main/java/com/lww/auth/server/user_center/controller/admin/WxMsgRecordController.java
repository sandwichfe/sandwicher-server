package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.service.WxMsgRecordService;
import com.lww.auth.server.user_center.vo.WxMsgRecordVo;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-center/admin/wx-msg-record")
@Tag(name = "微信消息记录管理")
public class WxMsgRecordController {

    @Resource
    private WxMsgRecordService wxMsgRecordService;

    @GetMapping("/page")
    @Operation(summary = "分页查询微信消息记录")
    public ResponseResult<IPage<WxMsgRecordVo>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResultUtil.success(wxMsgRecordService.listWxMsgRecord(current, size));
    }
}
