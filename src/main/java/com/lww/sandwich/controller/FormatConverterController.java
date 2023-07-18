package com.lww.sandwich.controller;

import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
import com.lww.sandwich.utils.ymlUtil.YamlPropertiesConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 *
 * 
 * @author
 */
@RestController
@RequestMapping("/formatConvert")
public class FormatConverterController
{
    @PostMapping("/propToYml")
    public ResponseResult propToYml(String propStr)
    {
        String convertedYamlString = null;
        try {
            convertedYamlString = YamlPropertiesConverter.convertPropertiesToYaml(propStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success(convertedYamlString);
    }

    @PostMapping("/ymlToProp")
    public ResponseResult ymlToProp(String ymlStr)
    {
        String convertedPropertiesString = null;
        try {
            convertedPropertiesString = YamlPropertiesConverter.convertYamlToProperties(ymlStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success(convertedPropertiesString);
    }
}
