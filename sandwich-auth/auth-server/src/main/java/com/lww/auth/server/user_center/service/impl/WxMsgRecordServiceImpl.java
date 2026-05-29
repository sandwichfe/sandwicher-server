package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.WxMsgRecord;
import com.lww.auth.server.user_center.mapper.WxMsgRecordMapper;
import com.lww.auth.server.user_center.service.WxMsgRecordService;
import org.springframework.stereotype.Service;

@Service
public class WxMsgRecordServiceImpl extends ServiceImpl<WxMsgRecordMapper, WxMsgRecord> implements WxMsgRecordService {
}
