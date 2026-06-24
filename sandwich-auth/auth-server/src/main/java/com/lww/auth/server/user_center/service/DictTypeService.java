package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.DictType;
import com.lww.auth.server.user_center.req.DictTypeReq;
import com.lww.auth.server.user_center.vo.DictTypePageQuery;
import com.lww.auth.server.user_center.vo.DictTypeVo;

import java.util.List;

public interface DictTypeService extends IService<DictType> {

    DictTypeVo createDictType(DictTypeReq req);

    DictTypeVo getDictTypeById(Long id);

    IPage<DictTypeVo> listDictType(DictTypePageQuery query);

    List<DictTypeVo> listAllDictType();

    DictTypeVo updateDictType(DictTypeReq req);

    void deleteType(Long dictTypeId);
}
