package com.lww.sandwich.controller.test;

import com.lww.sandwich.entity.DictType;
import com.lww.response.ResponseResult;
import com.lww.response.ResultUtil;
import com.lww.sandwich.service.DictTypeService;
import com.lww.sandwich.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *  
 * @author lww
 * @since 2022/4/21 14:36
 */

@RestController
@RequestMapping("/test")
@Api("test")
@Slf4j
public class TestController {

    @Resource
    private DictTypeService dictTypeService;

    @Resource(name = "ip2regionSearcher")
    private Ip2regionSearcher regionSearcher;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/listAllDictType")
    @ApiOperation(value = "返回所有")
    public ResponseResult<List<DictType>> listAllDictType() {
        List<DictType> data = dictTypeService.list();
        return ResultUtil.success(data);
    }

    @GetMapping("/testGetIp")
    @ApiOperation(value = "返回所有")
    public ResponseResult testGetIp(HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        String cityInfo = regionSearcher.getAddress("49.235.149.110");

        redisTemplate.opsForValue().set("cityInfo", cityInfo);
        Object str = redisTemplate.opsForValue().get("cityInfo");
        System.out.println(str);

        return ResultUtil.success(Map.of("cityInfo", cityInfo, "ipAddress", ipAddress));
    }


}
