package com.lww.sandwich;

import java.util.List;
import java.util.Map;

import com.lww.common.utils.ServletRequestUtil;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.redis.util.RedisUtil;
import com.lww.sandwich.entity.DictType;
import com.lww.sandwich.service.DictTypeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *  
 * @author lww
 * @since 2022/4/21 14:36
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestController {

    @Resource
    private DictTypeService dictTypeService;

    @Resource(name = "ip2regionSearcher")
    private Ip2regionSearcher regionSearcher;

    @Resource
    private RedisUtil redisUtil;

    public ResponseResult<List<DictType>> listAllDictType() {
        List<DictType> data = dictTypeService.list();
        return ResultUtil.success(data);
    }

    public ResponseResult<Object> testGetIp(HttpServletRequest request) {
        String ipAddress = ServletRequestUtil.getClientIp(request);
        String cityInfo = regionSearcher.getAddress("49.235.149.110");

        Object str = redisUtil.get("cityInfo");
        System.out.println(str);

        return ResultUtil.success(Map.of("cityInfo", cityInfo, "ipAddress", ipAddress));
    }


}
