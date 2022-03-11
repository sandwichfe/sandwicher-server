package com.lww.sandwich.service.impl;

import com.lww.sandwich.entity.Simple;
import com.lww.sandwich.mapper.SimpleMapper;
import com.lww.sandwich.service.SimpleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 例子 服务实现类
 * @author lww
 * @since 2022-03-11 16:04:27
 */
@Service
public class SimpleServiceImpl extends ServiceImpl<SimpleMapper, Simple> implements SimpleService {

}
