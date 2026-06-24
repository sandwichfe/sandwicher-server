package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.WxMsgRecord;
import com.lww.auth.server.user_center.vo.WxMsgRecordVo;

public interface WxMsgRecordService extends IService<WxMsgRecord> {

    IPage<WxMsgRecordVo> listWxMsgRecord(Integer current, Integer size);
}
