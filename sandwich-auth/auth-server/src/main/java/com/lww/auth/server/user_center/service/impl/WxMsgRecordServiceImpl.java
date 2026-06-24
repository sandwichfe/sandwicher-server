package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.WxMsgRecord;
import com.lww.auth.server.user_center.mapper.WxMsgRecordMapper;
import com.lww.auth.server.user_center.service.WxMsgRecordService;
import com.lww.auth.server.user_center.vo.WxMsgRecordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class WxMsgRecordServiceImpl extends ServiceImpl<WxMsgRecordMapper, WxMsgRecord> implements WxMsgRecordService {

    @Override
    public IPage<WxMsgRecordVo> listWxMsgRecord(Integer current, Integer size) {
        Page<WxMsgRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<WxMsgRecord> wrapper = new LambdaQueryWrapper<WxMsgRecord>()
                .orderByDesc(WxMsgRecord::getReceiveTime);
        return this.page(page, wrapper).convert(this::convertToVo);
    }

    private WxMsgRecordVo convertToVo(WxMsgRecord record) {
        WxMsgRecordVo vo = new WxMsgRecordVo();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }
}
