package com.lww.littlenote.controller;


import com.lww.littlenote.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lww
 */
@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/list1")
    public Map<String, Object> hello(String path, String keyword) {
        List<Map<String, String>> fileList = fileService.getFileList(path, keyword);
        Map<String, Object> map = new HashMap<>();
        map.put("data", fileList);
        return map;
    }

    @DeleteMapping("/deleteFile")
    public Map<String, Object> deleteFile(String path){
        fileService.deleteFile(path);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "删除成功!");
        map.put("code", 200);
        return map;
    }

    @PutMapping("/renameFile")
    public Map<String, Object> renameFile(String path,String newName){
        fileService.renameFile(path,newName);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "修改成功!");
        map.put("code", 200);
        return map;
    }

    @PutMapping("/mkdir")
    public Map<String, Object> mkdir(String path,String dirName){
        fileService.mkdir(path,dirName);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "创建成功!");
        map.put("code", 200);
        return map;
    }

    @GetMapping("/test")
    public Map<String, Object> test(String path, String keyword) {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "hello world!");
        map.put("code", 200);
        return map;
    }

}




