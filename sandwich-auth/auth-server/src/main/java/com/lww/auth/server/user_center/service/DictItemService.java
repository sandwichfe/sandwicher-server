package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.DictItem;
import com.lww.auth.server.user_center.req.DictItemReq;
import com.lww.auth.server.user_center.vo.DictItemPageQuery;
import com.lww.auth.server.user_center.vo.DictItemVo;

import java.util.List;

public interface DictItemService extends IService<DictItem> {

    DictItemVo createDictItem(DictItemReq req);

    DictItemVo getDictItemById(Long id);

    IPage<DictItemVo> listDictItem(DictItemPageQuery query);

    List<DictItemVo> listAllByDictTypeId(Long dictTypeId);

    DictItemVo updateDictItem(DictItemReq req);

    void deleteDictItem(Long id);
}
