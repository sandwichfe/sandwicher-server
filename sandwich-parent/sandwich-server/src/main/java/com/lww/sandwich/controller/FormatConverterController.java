package com.lww.sandwich.controller;

import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.utils.yml.YamlPropertiesConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * 
 * @author
 */
@Slf4j
@RestController
@RequestMapping("/formatConvert")
public class FormatConverterController
{
    @PostMapping("/propToYml")
    public ResponseResult<String> propToYml(String propStr)
    {
        String convertedYamlString = null;
        try {
            convertedYamlString = YamlPropertiesConverter.convertPropertiesToYaml(propStr);
        } catch (Exception e) {
            log.info("error",e);
        }
        return ResultUtil.success(convertedYamlString);
    }

    @PostMapping("/ymlToProp")
    public ResponseResult<String> ymlToProp(String ymlStr)
    {
        String convertedPropertiesString = null;
        try {
            convertedPropertiesString = YamlPropertiesConverter.convertYamlToProperties(ymlStr);
        } catch (Exception e) {
            log.info("error",e);
        }
        return ResultUtil.success(convertedPropertiesString);
    }
}
