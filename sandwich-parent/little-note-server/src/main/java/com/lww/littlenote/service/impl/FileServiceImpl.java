package com.lww.littlenote.service.impl;

import com.lww.common.web.exception.AppException;
import com.lww.littlenote.service.FileService;
import com.lww.littlenote.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 *  
 * @author lww
 * @since 2022/9/5 14:59
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public List<Map<String, String>> getFileList(String path, String keyword) {
        List<Map<String, String>> contents;
        //返回根路径 列出所有盘符
        if ("root".equals(path)) {
            contents = listDisk();
        } else {
            // 返回某文件/文件夹路径
            contents = getContents(path);
            // keyword搜索
            if (StringUtils.hasText(keyword)) {
                contents = queryByKeyWord(keyword, contents);
            }
        }
        return contents;
    }

    @Override
    public void deleteFile(String path) {
        if (!StringUtils.hasText(path) || !new File(path).exists()) {
            throw new AppException("此路径不存在！");
        }
        //删除文件
        FileUtils.delFiles(new File(path));
    }

    @Override
    public void renameFile(String path,String newName) {
        if (!StringUtils.hasText(path) || !new File(path).exists()) {
            throw new AppException("此路径不存在！");
        }
        File file = new File(path);
        String newPathName = path.substring(0,path.lastIndexOf(File.separator))+File.separator+newName;
        file.renameTo(new File(newPathName));
    }

    @Test
    public void tt(){
        String path = "F:\\apache-maven-3.5.4\\repository";
        System.out.println(path.substring(0,path.lastIndexOf(File.separator)));
    }


    /**
     * 当前主机所有磁盘 盘符
     * @author lww
     * @since 2022/8/26 13:26
     */
    public List<Map<String, String>> listDisk() {
        List<Map<String, String>> ret = new ArrayList<>();
        File[] files = File.listRoots();
        Map<String, String> cont;
        if (files != null) {
            for (File file : files) {
                cont = new HashMap<>();
                cont.put("type", "folder");
                cont.put("path", file.toString().replace(File.separator, ""));
                cont.put("preName", file.toString().replace(File.separator, ""));
                ret.add(cont);
            }
        }
        return ret;
    }


    private List<Map<String, String>> queryByKeyWord(String keyword, List<Map<String, String>> contents) {
        if (contents == null || contents.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String, String>> ret = new ArrayList<>();
        StringBuilder stringBuilder;
        for (Map<String, String> content : contents) {
            stringBuilder = new StringBuilder(content.get("preName"));
            if (stringBuilder.indexOf(keyword) != -1) {
                ret.add(content);
            }
        }
        return ret;
    }


    /**
     * @Author：
     *  ：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
     * @Date：
     */
    public List<Map<String, String>> getContents(String path) {
        // 盘符根路径 处理
        if (Arrays.asList("A:", "B:", "C:", "D:", "E:", "F:", "G:", "H:", "I:", "J:", "K:", "L:", "M:", "N:", "O:", "P:", "Q:", "R:", "S:", "T:", "U:", "V:", "W:", "X:", "Y:", "Z:").contains(path)) {
            path = path + "/";
        }
        List<Map<String, String>> contents = new ArrayList<>();
        File file = new File(path);
        // 不存在此路径直接返回空
        if (!file.exists()) {
            return Collections.emptyList();
        }
        File[] tempList = file.listFiles();
        Map<String, String> cont;
        for (int i = 0; i < Objects.requireNonNull(tempList).length; i++) {
            cont = new HashMap<>();
            if (tempList[i].isFile()) {
                cont.put("type", "file");
                cont.put("path", tempList[i].toString());
                cont.put("preName", tempList[i].getName());
            }
            if (tempList[i].isDirectory()) {
                cont.put("type", "folder");
                cont.put("path", tempList[i].toString());
                cont.put("preName", tempList[i].getName());
            }
            contents.add(cont);
        }
        return contents;
    }

    @Override
    public void mkdir(String path, String dirName) {
        if (!StringUtils.hasText(path)|| !StringUtils.hasText(dirName)){
            throw new AppException("传个空参数干哈勒！");
        }
        String dirPath = path+File.separator+dirName;
        File file = new File(dirPath);
        if (file.exists()){
            String fileType = file.isDirectory()?"文件夹":"文件";
            throw new AppException("当前目录已存在此"+fileType+"!");
        }
        boolean ret = file.mkdir();
        if (!ret){
            throw new AppException("创建失败！");
        }
    }
}
