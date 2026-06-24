package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.vo.OperationLogPageQuery;
import com.lww.auth.server.user_center.vo.OperationLogVo;

public interface AdminOperationLogService {

    IPage<OperationLogVo> listOperationLog(OperationLogPageQuery query);
}
