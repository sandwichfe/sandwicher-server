package com.lww.sandwich.controller;

import com.lww.response.ResponseResult;
import com.lww.response.ResultUtil;
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
    public ResponseResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return ResultUtil.success(server);
    }
}
