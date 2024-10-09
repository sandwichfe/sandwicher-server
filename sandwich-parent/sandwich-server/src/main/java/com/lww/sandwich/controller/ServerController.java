package com.lww.sandwich.controller;

import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.entity.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @GetMapping()
    public ResponseResult<Server> getInfo() {
        Server server = new Server();
        server.copyTo();
        return ResultUtil.success(server);
    }
}
