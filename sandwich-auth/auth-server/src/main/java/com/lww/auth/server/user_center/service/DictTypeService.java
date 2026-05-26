package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.DictType;

public interface DictTypeService extends IService<DictType> {

    void deleteType(Long dictTypeId);
}

