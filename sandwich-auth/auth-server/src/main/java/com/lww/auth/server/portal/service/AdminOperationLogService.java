package com.lww.auth.server.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.portal.vo.OperationLogPageQuery;
import com.lww.auth.server.portal.vo.OperationLogVo;

public interface AdminOperationLogService {

    IPage<OperationLogVo> listOperationLog(OperationLogPageQuery query);
}
