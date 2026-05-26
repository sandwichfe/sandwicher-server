package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.DictItem;
import com.lww.auth.server.user_center.mapper.DictItemMapper;
import com.lww.auth.server.user_center.service.DictItemService;
import org.springframework.stereotype.Service;

@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {
}

