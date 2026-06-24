package com.lww.auth.server.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.portal.entity.WxMsgRecord;
import com.lww.auth.server.portal.vo.WxMsgRecordVo;

public interface WxMsgRecordService extends IService<WxMsgRecord> {

    IPage<WxMsgRecordVo> listWxMsgRecord(Integer current, Integer size);
}
