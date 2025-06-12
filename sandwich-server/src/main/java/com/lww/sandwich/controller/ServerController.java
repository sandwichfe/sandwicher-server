package com.lww.sandwich.controller;

import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.entity.server.ServerInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 * 
 * @author ruoyi
 */
@Tag(name = "服务器信息", description = "服务器信息")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class ServerController {

    @Operation(summary = "服务器信息")
    @GetMapping("/serverInfo")
    public ResponseResult<ServerInfo> getServerInfo() {
        return ResultUtil.success(ServerInfo.fillInfo());
    }
}
