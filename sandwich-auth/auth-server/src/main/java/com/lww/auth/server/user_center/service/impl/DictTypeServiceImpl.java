package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.DictItem;
import com.lww.auth.server.user_center.entity.DictType;
import com.lww.auth.server.user_center.mapper.DictItemMapper;
import com.lww.auth.server.user_center.mapper.DictTypeMapper;
import com.lww.auth.server.user_center.service.DictTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    private DictItemMapper dictItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteType(Long dictTypeId) {
        dictItemMapper.delete(new LambdaQueryWrapper<DictItem>().eq(DictItem::getDictTypeId, dictTypeId));
        this.removeById(dictTypeId);
    }
}

