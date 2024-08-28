package com.lww.sandwich.controller;

import com.lww.sandwich.entity.Server;
import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return ResultUtil.success(server);
    }
}
