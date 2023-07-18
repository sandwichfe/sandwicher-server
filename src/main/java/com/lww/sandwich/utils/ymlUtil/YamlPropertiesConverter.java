package com.lww.sandwich.utils.ymlUtil;

/**
 * @description:
 * @author lww
 * @since 2023/7/18 13:17
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class YamlPropertiesConverter {
    public static void main(String[] args) {
        String yamlString = "key1:\n  key2:\n    key3: value";

        try {
            // 将YAML转换为Properties格式的字符串
            String propertiesString = convertYamlToProperties(yamlString);
            System.out.println("Properties格式的字符串：");
            System.out.println(propertiesString);

            // 将Properties转换为YAML格式的字符串
            String yamlStringConverted = convertPropertiesToYaml(propertiesString);
            System.out.println("YAML格式的字符串：");
            System.out.println(yamlStringConverted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertYamlToProperties(String yamlString) throws IOException {
        // 将YAML字符串转换为Properties
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Object yamlObject = objectMapper.readValue(yamlString, Object.class);

        // 将YAML对象转换为Properties
        Properties properties = new Properties();
        flattenYamlObject(properties, "", yamlObject);

        // 将Properties转换为字符串
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        properties.store(outputStream, null);

        return outputStream.toString();
    }

    public static String convertPropertiesToYaml(String propertiesString) throws IOException {
        // 将Properties格式的字符串转换为Properties对象
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(propertiesString.getBytes()));

        // 将Properties转换为YAML对象
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Object yamlObject = expandProperties(properties);

        // 将YAML对象转换为字符串
        try {
            return objectMapper.writeValueAsString(yamlObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void flattenYamlObject(Properties properties, String prefix, Object yamlObject) {
        if (yamlObject instanceof LinkedHashMap) {
            LinkedHashMap<?, ?> yamlMap = (LinkedHashMap<?, ?>) yamlObject;
            for (Map.Entry<?, ?> entry : yamlMap.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
                flattenYamlObject(properties, fullKey, value);
            }
        } else {
            properties.put(prefix, yamlObject.toString());
        }
    }

    private static Object expandProperties(Properties properties) {
        LinkedHashMap<String, Object> yamlObject = new LinkedHashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String[] parts = key.split("\\.");
            LinkedHashMap<String, Object> currentMap = yamlObject;

            for (int i = 0; i < parts.length - 1; i++) {
                String part = parts[i];
                LinkedHashMap<String, Object> childMap = (LinkedHashMap<String, Object>) currentMap.getOrDefault(part, new LinkedHashMap<>());
                currentMap.put(part, childMap);
                currentMap = childMap;
            }

            currentMap.put(parts[parts.length - 1], properties.getProperty(key));
        }

        return yamlObject;
    }
}
