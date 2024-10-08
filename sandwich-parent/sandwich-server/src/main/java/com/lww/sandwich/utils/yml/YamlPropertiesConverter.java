package com.lww.sandwich.utils.yml;

import com.lww.common.web.exception.AppException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Yaml 配置文件转 Properties 配置文件工具类
 * @author <a href="https://zyqok.blog.csdn.net/">作者</a>
 * @since 2021/08/24
 */
@Slf4j
public class YamlPropertiesConverter {
    private static final String LINE_SPLIT = "\n";

    private YamlPropertiesConverter(){
    }

    /**
     * 将 yml 文件转化为 properties 文件
     *
     * @param ymlFileName 工程根目录下（非resources目录）的 yml 文件名称（如：abc.yml）
     * @return List<Node> 每个Nyml 文件中每行对应解析的数据
     */
    public static List<YmlNode> castProperties(String ymlFileName) {
        if (ymlFileName == null || !ymlFileName.endsWith(".yml")) {
            throw new AppException("请输入yml文件名称！！");
        }
        File ymlFile = new File(ymlFileName);
        if (!ymlFile.exists()) {
            throw new AppException("工程根目录下不存在 " + ymlFileName + "文件！！");
        }
        String fileName = ymlFileName.split(".yml", 2)[0];
        // 获取文件数据
        String yml = read(ymlFile);
        List<YmlNode> nodeList = getNodeList(yml);
        // 去掉多余数据，并打印
        String str = printNodeList(nodeList);
        // 将数据写入到 properties 文件中
        String propertiesName = fileName + ".properties";
        File file = new File(propertiesName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.info("error",e);
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            log.info("error",e);
        }
        return nodeList;
    }

    /**
     * yml转properties
     * @author lww
     * @since 2023/8/3 16:23
     * @param ymlStr ymlStr
     */
   public static String convertYamlToProperties(String ymlStr){
       List<YmlNode> nodeList = getNodeList(ymlStr);
       // 去掉多余数据，并打印
       return printNodeList(nodeList);
    }

    /**
     * properties转yml
     * @param propertiesStr propertiesStr
     * @return String
     */
    public static String convertPropertiesToYaml(String propertiesStr){
        //保存yml的行内容
        StringBuilder ymlLines = new StringBuilder();
        List<String> lines = new ArrayList<>(Arrays.asList(propertiesStr.split(LINE_SPLIT)));
        //使用treemap排好序
        Map<String,String> sourceMap = new LinkedHashMap<>();
        long commentLine = 0L;
        long emptyLine = 0L;
        for (String line:lines){
            String key;
            String value;
            // 注释
            if (line.startsWith("#")){
                key = "comment-text-"+commentLine++;
                value = line;
                sourceMap.put(key,value);
            }
            else if (!StringUtils.hasText(line)){
                key = "empty-text-"+emptyLine++;
                value = line;
                sourceMap.put(key,value);
            }
            else {
                key = line.substring(0,line.indexOf("="));
                value = line.substring(line.indexOf("=")+1);
                sourceMap.put(key,value);
            }
        }
        Iterator<String> it = sourceMap.keySet().iterator();
        //Tab用两个空格格式化
        String tab = "  ";

        //yml文档树
        Map<String,List<String>> treeMap = new TreeMap<>();
        //父级名称
        String parent = "";
        //子节点列表
        List<String> element;
        while(it.hasNext()){
            String key = it.next();
            //.拆分key
            String[] keys = key.split("\\.");
            String prefix = "";
            for (int i=0;i<keys.length;i++){
                //从第二个节点开始，行前面需要加tab来格式化，并设置它的父节点
                if (i>0){
                    parent+=keys[i-1];
                    prefix += tab;
                }
                String line = prefix + keys[i]+ ": ";
                treeMap.computeIfAbsent(parent, k -> new ArrayList<>());
                if(!treeMap.get(parent).contains(line)){
                    element = treeMap.get(parent)==null? new ArrayList<>():treeMap.get(parent);
                    if (!element.contains(line)){
                        element.add(line);
                        treeMap.put(parent,element);
                    }
                    if (i==keys.length-1){
                        // 如果是注释或者空行
                        if (line.startsWith("comment-text")||line.startsWith("empty-text")){
                            ymlLines.append(sourceMap.get(key)).append(LINE_SPLIT);
                        }else{
                            ymlLines.append(line+sourceMap.get(key)).append(LINE_SPLIT);
                        }
                        parent = "";
                    }else{
                        ymlLines.append(line).append(LINE_SPLIT);
                    }
                }
            }
        }
        return ymlLines.toString();
    }
    
    /**
     * 将yml转化为porperties文件，并获取转化后的键值对
     *
     * @param ymlFileName 工程根目录下的 yml 文件名称
     * @return 转化后的 porperties 文件键值对Map
     */
    public static Map<String, String> getPropertiesMap(String ymlFileName) {
        Map<String, String> map = new HashMap<>();
        List<YmlNode> list = castProperties(ymlFileName);
        for (YmlNode node : list) {
            if (!node.getKey().isEmpty()) {
                map.put(node.getKey(), node.getValue());
            }
        }
        return map;
    }

    private static String read(File file) {
        if (Objects.isNull(file) || !file.exists()) {
            return "";
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] b = new byte[(int) file.length()];
            fis.read(b);
            return new String(b, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.info("error",e);
        }
        return "";
    }

    private static String printNodeList(List<YmlNode> nodeList) {
        StringBuilder sb = new StringBuilder();
        for (YmlNode node : nodeList) {
            if (node.getLast().equals(Boolean.FALSE)) {
                continue;
            }
            if (node.getEmptyLine().equals(Boolean.TRUE)) {
                sb.append(LINE_SPLIT);
                continue;
            }
            // 判断是否有行级注释
            if (!node.getHeadRemark().isEmpty()) {
                String s = "# " + node.getHeadRemark();
                sb.append(s).append(LINE_SPLIT);
                continue;
            }
            // 判断是否有行末注释 (properties中注释不允许末尾注释，故而放在上面)
            if (!node.getTailRemark().isEmpty()) {
                String s = "# " + node.getTailRemark();
                sb.append(s).append(LINE_SPLIT);
            }
            String kv = node.getKey() + "=" + node.getValue();
            sb.append(kv).append(LINE_SPLIT);
        }
        return sb.toString();
    }

    private static List<YmlNode> getNodeList(String yml) {
        String[] lines = yml.split(LINE_SPLIT);
        List<YmlNode> nodeList = new ArrayList<>();
        Map<Integer, String> keyMap = new HashMap<>();
        Set<String> keySet = new HashSet<>();
        for (String line : lines) {
            YmlNode node = getNode(line);
            if (node.getKey() != null && !node.getKey().isEmpty()) {
                int level = node.getLevel();
                if (level == 0) {
                    keyMap.clear();
                    keyMap.put(0, node.getKey());
                } else {
                    int parentLevel = level - 1;
                    String parentKey = keyMap.get(parentLevel);
                    String currentKey = parentKey + "." + node.getKey();
                    keyMap.put(level, currentKey);
                    node.setKey(currentKey);
                }
            }
            keySet.add(node.getKey() + ".");
            nodeList.add(node);
        }
        // 标识是否最后一级
        for (YmlNode each : nodeList) {
            each.setLast(getNodeLast(each.getKey(), keySet));
        }
        return nodeList;
    }

    private static boolean getNodeLast(String key, Set<String> keySet) {
        if (key.isEmpty()) {
            return true;
        }
        key = key + ".";
        int count = 0;
        for (String each : keySet) {
            if (each.startsWith(key)) {
                count++;
            }
        }
        return count == 1;
    }

    private static YmlNode getNode(String line) {
        YmlNode node = new YmlNode();
        // 初始化默认数据（防止NPE）
        node.setEffective(Boolean.FALSE);
        node.setEmptyLine(Boolean.FALSE);
        node.setHeadRemark("");
        node.setKey("");
        node.setValue("");
        node.setTailRemark("");
        node.setLast(Boolean.FALSE);
        node.setLevel(0);
        // 空行，不处理
        String trimStr = line.trim();
        if (trimStr.isEmpty()) {
            node.setEmptyLine(Boolean.TRUE);
            return node;
        }
        // 行注释，不处理
        if (trimStr.startsWith("#")) {
            node.setHeadRemark(trimStr.replaceFirst("#", "").trim());
            return node;
        }
        // 处理值
        String[] strs = line.split(":", 2);
        // 拆分后长度为0的，属于异常数据，不做处理
        if (strs.length == 0) {
            return node;
        }
        // 获取键
        node.setKey(strs[0].trim());
        // 获取值
        String value;
        if (strs.length == 2) {
            value = strs[1];
        } else {
            value = "";
        }
        // 获取行末备注
        String tailRemark = "";
        if (value.contains(" #")) {
            String[] vs = value.split("#", 2);
            if (vs.length == 2) {
                value = vs[0];
                tailRemark = vs[1];
            }
        }
        node.setTailRemark(tailRemark.trim());
        node.setValue(value.trim());
        // 获取当前层级
        int level = getNodeLevel(line);
        node.setLevel(level);
        node.setEffective(Boolean.TRUE);
        return node;
    }

    private static int getNodeLevel(String line) {
        if (line.trim().isEmpty()) {
            return 0;
        }
        char[] chars = line.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (c != ' ') {
                break;
            }
            count++;
        }
        return count / 2;
    }
}

@Setter
@Getter
 class YmlNode {

    /** 层级关系 */
    private Integer level;
    /** 键 */
    private String key;
    /** 值 */
    private String value;
    /** 是否为空行 */
    private Boolean emptyLine;
    /** 当前行是否为有效配置 */
    private Boolean effective;
    /** 头部注释（单行注释） */
    private String headRemark;
    /** 末尾注释 */
    private String tailRemark;
    /** 是否为最后一层配置 */
    private Boolean last;

}