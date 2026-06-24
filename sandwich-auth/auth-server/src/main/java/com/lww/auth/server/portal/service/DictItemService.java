package com.lww.auth.server.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.portal.entity.DictItem;
import com.lww.auth.server.portal.req.DictItemReq;
import com.lww.auth.server.portal.vo.DictItemPageQuery;
import com.lww.auth.server.portal.vo.DictItemVo;

import java.util.List;

public interface DictItemService extends IService<DictItem> {

    DictItemVo createDictItem(DictItemReq req);

    DictItemVo getDictItemById(Long id);

    IPage<DictItemVo> listDictItem(DictItemPageQuery query);

    List<DictItemVo> listAllByDictTypeId(Long dictTypeId);

    DictItemVo updateDictItem(DictItemReq req);

    void deleteDictItem(Long id);
}
